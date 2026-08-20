#!/usr/bin/env python3
"""
Agent Bridge Fast-MCP Server (Stdio)
-----------------------------------
Exposes Agent Bridge coordination tools directly to Antigravity and OpenCode:
- create_task
- get_task
- update_task
- complete_task
- list_tasks
- read_memory
- write_memory
- get_report
- get_agent_status
- audit_batch_yield (Antigravity PM Quality Gate)
- trigger_claude_next_batch (Instant Claude Code CLI invocation)
"""

import sys
import os
import json

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, BASE_DIR)
from agent_bridge import AgentBridge
from pm_audit_bridge import pm_audit_yield, trigger_claude_cli_next_batch

bridge = AgentBridge()

def send_response(response_id, result=None, error=None):
    resp = {"jsonrpc": "2.0", "id": response_id}
    if error:
        resp["error"] = error
    else:
        resp["result"] = result
    sys.stdout.write(json.dumps(resp) + "\n")
    sys.stdout.flush()

def handle_initialize(req_id, params):
    result = {
        "protocolVersion": "2024-11-05",
        "capabilities": {
            "tools": {}
        },
        "serverInfo": {
            "name": "agent-bridge-mcp",
            "version": "1.1.0"
        }
    }
    send_response(req_id, result)

def handle_tools_list(req_id):
    tools = [
        {
            "name": "create_task",
            "description": "Create a new assigned task in the .ai orchestration system.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string", "description": "Unique task ID"},
                    "objective": {"type": "string", "description": "Objective"},
                    "context": {"type": "string"},
                    "assigned_to": {"type": "string", "default": "opencode"},
                    "scope_files": {"type": "array", "items": {"type": "string"}},
                    "constraints": {"type": "array", "items": {"type": "string"}},
                    "acceptance_criteria": {"type": "array", "items": {"type": "string"}}
                },
                "required": ["task_id", "objective"]
            }
        },
        {
            "name": "get_task",
            "description": "Fetch a task's full state.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string"}
                },
                "required": ["task_id"]
            }
        },
        {
            "name": "update_task",
            "description": "Update task status (IN_PROGRESS, REVIEW, REASSIGNED, COMPLETED).",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string"},
                    "status": {"type": "string"},
                    "notes": {"type": "string"},
                    "assigned_to": {"type": "string"},
                    "report_file": {"type": "string"}
                },
                "required": ["task_id"]
            }
        },
        {
            "name": "complete_task",
            "description": "Mark a task as COMPLETED with an attached report.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string"},
                    "report_file": {"type": "string"},
                    "result": {"type": "string"}
                },
                "required": ["task_id"]
            }
        },
        {
            "name": "list_tasks",
            "description": "List all tasks filtered by status or assignee.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "status": {"type": "string"},
                    "assigned_to": {"type": "string"}
                }
            }
        },
        {
            "name": "read_memory",
            "description": "Read permanent project memory from .ai/MEMORY.md.",
            "inputSchema": {
                "type": "object",
                "properties": {}
            }
        },
        {
            "name": "write_memory",
            "description": "Append knowledge to .ai/MEMORY.md.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "entry": {"type": "string"},
                    "category": {"type": "string", "default": "General Knowledge"}
                },
                "required": ["entry"]
            }
        },
        {
            "name": "get_report",
            "description": "Read an execution report from .ai/reports/.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "report_id": {"type": "string"}
                },
                "required": ["report_id"]
            }
        },
        {
            "name": "get_agent_status",
            "description": "Get high-level summary of active tasks.",
            "inputSchema": {
                "type": "object",
                "properties": {}
            }
        },
        {
            "name": "audit_batch_yield",
            "description": "Antigravity PM Quality Gate: Audits Claude Expected Table vs OpenCode Parsed Table, verifies $0.00 match or reassigns task to OpenCode.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "batch_id": {"type": "string", "description": "Optional batch ID"}
                }
            }
        },
        {
            "name": "trigger_claude_next_batch",
            "description": "Directly invokes the connected Claude Code CLI on the local machine to generate and push the next batch.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "batch_num": {"type": "integer", "description": "Batch number to generate, e.g. 6"},
                    "instructions": {"type": "string", "description": "Optional specific focus instructions"}
                },
                "required": ["batch_num"]
            }
        }
    ]
    send_response(req_id, {"tools": tools})

def handle_tool_call(req_id, params):
    name = params.get("name")
    args = params.get("arguments", {})

    try:
        if name == "create_task":
            res = bridge.create_task(
                task_id=args.get("task_id"),
                objective=args.get("objective"),
                context=args.get("context", ""),
                assigned_to=args.get("assigned_to", "opencode"),
                scope_files=args.get("scope_files", []),
                constraints=args.get("constraints", []),
                acceptance_criteria=args.get("acceptance_criteria", [])
            )
        elif name == "get_task":
            res = bridge.get_task(args.get("task_id"))
        elif name == "update_task":
            res = bridge.update_task(
                task_id=args.get("task_id"),
                status=args.get("status"),
                notes=args.get("notes", ""),
                assigned_to=args.get("assigned_to"),
                report_file=args.get("report_file")
            )
        elif name == "complete_task":
            res = bridge.complete_task(
                task_id=args.get("task_id"),
                report_file=args.get("report_file"),
                result=args.get("result")
            )
        elif name == "list_tasks":
            res = bridge.list_tasks(
                status=args.get("status"),
                assigned_to=args.get("assigned_to")
            )
        elif name == "read_memory":
            res = {"content": bridge.read_memory()}
        elif name == "write_memory":
            res = bridge.write_memory(
                entry=args.get("entry"),
                category=args.get("category", "General Knowledge")
            )
        elif name == "get_report":
            res = bridge.get_report(args.get("report_id"))
        elif name == "get_agent_status":
            res = bridge.get_agent_status()
        elif name == "audit_batch_yield":
            res = pm_audit_yield(args.get("batch_id"))
        elif name == "trigger_claude_next_batch":
            res = trigger_claude_cli_next_batch(args.get("batch_num"), args.get("instructions"))
        else:
            send_response(req_id, error={"code": -32601, "message": f"Tool '{name}' not found"})
            return

        send_response(req_id, {
            "content": [
                {
                    "type": "text",
                    "text": json.dumps(res, indent=2)
                }
            ]
        })
    except Exception as e:
        send_response(req_id, error={"code": -32000, "message": str(e)})

def main():
    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue
        try:
            req = json.loads(line)
            req_id = req.get("id")
            method = req.get("method")
            params = req.get("params", {})

            if method == "initialize":
                handle_initialize(req_id, params)
            elif method == "notifications/initialized":
                pass
            elif method == "tools/list":
                handle_tools_list(req_id)
            elif method == "tools/call":
                handle_tool_call(req_id, params)
            elif method == "ping":
                send_response(req_id, {})
            else:
                if req_id is not None:
                    send_response(req_id, error={"code": -32601, "message": f"Method '{method}' not found"})
        except Exception as e:
            if "req_id" in locals() and req_id is not None:
                send_response(req_id, error={"code": -32700, "message": f"Parse error: {str(e)}"})

if __name__ == "__main__":
    main()
