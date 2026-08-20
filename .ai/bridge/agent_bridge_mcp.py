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
"""

import sys
import os
import json

# Add local directory to path
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, BASE_DIR)
from agent_bridge import AgentBridge

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
            "version": "1.0.0"
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
                    "task_id": {"type": "string", "description": "Unique task ID, e.g. TASK_001_UK_HOLIDAYS"},
                    "objective": {"type": "string", "description": "Clear, measurable objective for the task"},
                    "context": {"type": "string", "description": "Background context and architectural intent"},
                    "assigned_to": {"type": "string", "description": "Agent name, default 'opencode'"},
                    "scope_files": {"type": "array", "items": {"type": "string"}, "description": "List of files in scope"},
                    "constraints": {"type": "array", "items": {"type": "string"}, "description": "Execution constraints"},
                    "acceptance_criteria": {"type": "array", "items": {"type": "string"}, "description": "Verification acceptance criteria"}
                },
                "required": ["task_id", "objective"]
            }
        },
        {
            "name": "get_task",
            "description": "Fetch a task's full state, constraints, and acceptance criteria.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string", "description": "The task ID to fetch"}
                },
                "required": ["task_id"]
            }
        },
        {
            "name": "update_task",
            "description": "Update task status (IN_PROGRESS, REVIEW, BLOCKED), notes, or result.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string", "description": "Task ID"},
                    "status": {"type": "string", "enum": ["BACKLOG", "PLANNED", "ASSIGNED", "IN_PROGRESS", "REVIEW", "COMPLETED", "BLOCKED", "REASSIGNED"]},
                    "notes": {"type": "string", "description": "Progress notes or blocker details"},
                    "assigned_to": {"type": "string", "description": "Reassign to another agent"},
                    "report_file": {"type": "string", "description": "Report filename in .ai/reports/"}
                },
                "required": ["task_id"]
            }
        },
        {
            "name": "complete_task",
            "description": "Mark a task as COMPLETED with an attached report file.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "task_id": {"type": "string", "description": "Task ID"},
                    "report_file": {"type": "string", "description": "Path to report in .ai/reports/"},
                    "result": {"type": "string", "description": "Summary of output/result"}
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
                    "status": {"type": "string", "description": "Filter by status"},
                    "assigned_to": {"type": "string", "description": "Filter by assigned agent"}
                }
            }
        },
        {
            "name": "read_memory",
            "description": "Read the permanent project memory and context from .ai/MEMORY.md.",
            "inputSchema": {
                "type": "object",
                "properties": {}
            }
        },
        {
            "name": "write_memory",
            "description": "Append a new permanent finding or knowledge item to .ai/MEMORY.md.",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "entry": {"type": "string", "description": "Knowledge entry text"},
                    "category": {"type": "string", "description": "Category or topic header"}
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
                    "report_id": {"type": "string", "description": "Report filename or task ID"}
                },
                "required": ["report_id"]
            }
        },
        {
            "name": "get_agent_status",
            "description": "Get high-level summary of active tasks and orchestration health.",
            "inputSchema": {
                "type": "object",
                "properties": {}
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
                pass # no response needed for notifications
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
EOF
