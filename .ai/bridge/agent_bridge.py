#!/usr/bin/env python3
"""
Agent Bridge Core Engine & CLI
-----------------------------
Provides standardized task management and memory coordination between
Antigravity (Orchestrator) and OpenCode (Executor).

Tools Exposed:
- create_task(task_id, objective, context, assigned_to, scope_files, constraints, acceptance_criteria)
- get_task(task_id)
- update_task(task_id, status, notes, result)
- complete_task(task_id, report_file)
- list_tasks(status, assigned_to)
- read_memory(section)
- write_memory(entry, category)
- get_report(report_id)
- get_agent_status()
"""

import json
import os
import sys
import datetime

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
AI_DIR = os.path.join(BASE_DIR, ".ai")
TASKS_DIR = os.path.join(AI_DIR, "tasks")
REPORTS_DIR = os.path.join(AI_DIR, "reports")
MEMORY_FILE = os.path.join(AI_DIR, "MEMORY.md")
PROJECT_FILE = os.path.join(AI_DIR, "PROJECT.md")

VALID_STATUSES = ["BACKLOG", "PLANNED", "ASSIGNED", "IN_PROGRESS", "REVIEW", "COMPLETED", "BLOCKED", "REASSIGNED"]

def now_iso():
    return datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S")

class AgentBridge:
    def __init__(self):
        os.makedirs(TASKS_DIR, exist_ok=True)
        os.makedirs(REPORTS_DIR, exist_ok=True)

    def create_task(self, task_id, objective, context="", assigned_to="opencode", scope_files=None, constraints=None, acceptance_criteria=None):
        if scope_files is None:
            scope_files = []
        if constraints is None:
            constraints = []
        if acceptance_criteria is None:
            acceptance_criteria = []

        task_data = {
            "task_id": task_id,
            "objective": objective,
            "context": context,
            "assigned_to": assigned_to,
            "status": "ASSIGNED",
            "created_at": now_iso(),
            "updated_at": now_iso(),
            "scope_files": scope_files,
            "constraints": constraints,
            "acceptance_criteria": acceptance_criteria,
            "history": [
                {"timestamp": now_iso(), "action": "CREATED", "by": "antigravity", "notes": "Task initialized"}
            ],
            "result": None,
            "report_file": None
        }

        file_path = os.path.join(TASKS_DIR, f"{task_id}.json")
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(task_data, f, indent=2)
        return task_data

    def get_task(self, task_id):
        file_path = os.path.join(TASKS_DIR, f"{task_id}.json")
        if not os.path.exists(file_path):
            return {"error": f"Task {task_id} not found"}
        with open(file_path, "r", encoding="utf-8") as f:
            return json.load(f)

    def update_task(self, task_id, status=None, notes="", assigned_to=None, result=None, report_file=None):
        task = self.get_task(task_id)
        if "error" in task:
            return task

        if status:
            if status.upper() not in VALID_STATUSES:
                return {"error": f"Invalid status: {status}. Must be one of {VALID_STATUSES}"}
            task["status"] = status.upper()

        if assigned_to:
            task["assigned_to"] = assigned_to

        if result:
            task["result"] = result

        if report_file:
            task["report_file"] = report_file

        task["updated_at"] = now_iso()
        task["history"].append({
            "timestamp": now_iso(),
            "status": task["status"],
            "notes": notes,
            "assigned_to": task["assigned_to"]
        })

        file_path = os.path.join(TASKS_DIR, f"{task_id}.json")
        with open(file_path, "w", encoding="utf-8") as f:
            json.dump(task, f, indent=2)
        return task

    def complete_task(self, task_id, report_file=None, result=None):
        return self.update_task(
            task_id=task_id,
            status="COMPLETED",
            notes="Task marked completed by orchestrator/executor",
            result=result,
            report_file=report_file
        )

    def list_tasks(self, status=None, assigned_to=None):
        tasks = []
        for filename in sorted(os.listdir(TASKS_DIR)):
            if filename.endswith(".json"):
                with open(os.path.join(TASKS_DIR, filename), "r", encoding="utf-8") as f:
                    t = json.load(f)
                    if status and t.get("status", "").upper() != status.upper():
                        continue
                    if assigned_to and t.get("assigned_to", "").lower() != assigned_to.lower():
                        continue
                    tasks.append(t)
        return tasks

    def read_memory(self):
        if not os.path.exists(MEMORY_FILE):
            return "Memory file does not exist yet."
        with open(MEMORY_FILE, "r", encoding="utf-8") as f:
            return f.read()

    def write_memory(self, entry, category="General Knowledge"):
        timestamp = now_iso()
        formatted_entry = f"\n\n### [{timestamp}] {category}\n{entry}"
        with open(MEMORY_FILE, "a", encoding="utf-8") as f:
            f.write(formatted_entry)
        return {"status": "success", "category": category, "timestamp": timestamp}

    def get_report(self, report_id):
        file_path = os.path.join(REPORTS_DIR, report_id)
        if not os.path.exists(file_path):
            # check with .md extension
            file_path_md = os.path.join(REPORTS_DIR, f"{report_id}.md")
            if os.path.exists(file_path_md):
                file_path = file_path_md
            else:
                return {"error": f"Report {report_id} not found in {REPORTS_DIR}"}
        with open(file_path, "r", encoding="utf-8") as f:
            return {"report_id": report_id, "content": f.read()}

    def get_agent_status(self):
        tasks = self.list_tasks()
        summary = {
            "total_tasks": len(tasks),
            "by_status": {},
            "active_tasks": [],
            "opencode_running": False
        }
        for t in tasks:
            st = t.get("status", "UNKNOWN")
            summary["by_status"][st] = summary["by_status"].get(st, 0) + 1
            if st in ["ASSIGNED", "IN_PROGRESS", "REVIEW", "BLOCKED"]:
                summary["active_tasks"].append({
                    "task_id": t.get("task_id"),
                    "objective": t.get("objective"),
                    "status": st,
                    "assigned_to": t.get("assigned_to")
                })
        return summary

# CLI interface for command-line or subprocess execution
if __name__ == "__main__":
    bridge = AgentBridge()
    if len(sys.argv) < 2:
        print("Usage: python3 bridge.py <command> [args...]")
        print("Commands: create_task, get_task, update_task, complete_task, list_tasks, read_memory, write_memory, get_report, get_agent_status")
        sys.exit(1)

    cmd = sys.argv[1]
    
    if cmd == "create_task":
        # create_task <task_id> <objective> [context] [assigned_to]
        tid = sys.argv[2]
        obj = sys.argv[3]
        ctx = sys.argv[4] if len(sys.argv) > 4 else ""
        assigned = sys.argv[5] if len(sys.argv) > 5 else "opencode"
        res = bridge.create_task(tid, obj, context=ctx, assigned_to=assigned)
        print(json.dumps(res, indent=2))
        
    elif cmd == "get_task":
        tid = sys.argv[2]
        print(json.dumps(bridge.get_task(tid), indent=2))
        
    elif cmd == "update_task":
        # update_task <task_id> <status> [notes]
        tid = sys.argv[2]
        status = sys.argv[3]
        notes = sys.argv[4] if len(sys.argv) > 4 else ""
        print(json.dumps(bridge.update_task(tid, status=status, notes=notes), indent=2))

    elif cmd == "complete_task":
        tid = sys.argv[2]
        rep = sys.argv[3] if len(sys.argv) > 3 else None
        print(json.dumps(bridge.complete_task(tid, report_file=rep), indent=2))

    elif cmd == "list_tasks":
        status = sys.argv[2] if len(sys.argv) > 2 else None
        print(json.dumps(bridge.list_tasks(status=status), indent=2))

    elif cmd == "read_memory":
        print(bridge.read_memory())

    elif cmd == "write_memory":
        entry = sys.argv[2]
        cat = sys.argv[3] if len(sys.argv) > 3 else "General"
        print(json.dumps(bridge.write_memory(entry, cat), indent=2))

    elif cmd == "get_report":
        rid = sys.argv[2]
        print(json.dumps(bridge.get_report(rid), indent=2))

    elif cmd == "get_agent_status":
        print(json.dumps(bridge.get_agent_status(), indent=2))

    else:
        print(f"Unknown command: {cmd}")
        sys.exit(1)
