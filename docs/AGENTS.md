# PlateMate Agent Workflow

**DO NOT READ ALL DOCS IN `docs/`. Read ONLY what you need for the task.**

## Core Operating Rules
1. **Existing Pattern Wins:** If unsure, mimic how a similar feature is already built.
2. **Smallest Safe Change:** Do not refactor anything outside your direct task without approval.
3. **Uncertainty Rule:**
   * If uncertainty blocks a safe implementation, **ask the user**.
   * If it's a minor detail (e.g., standard padding), make a reasonable assumption and state it.

## Execution Order for Agents

When assigned a task, follow this exact order:

1. Check `docs/index.md` to find the 2-4 specific documents relevant to your task.
2. Read ONLY those selected documents.
3. Read `docs/multi-agent-workflow.md` ONLY if:
   - the task explicitly involves multiple agents,
   - you are assigned a specific role such as Planner, Implementer, Reviewer, or Docs Agent,
   - or the task is about changing the agent workflow itself.
4. If building a new feature, follow `docs/feature-development-playbook.md`.
5. Execute the smallest safe change.
6. Verify changes using `docs/build-run-test-guide.md` when verification is part of your role or the task requires it.

## Single-Agent Mode

If only one agent is working on the task, it must act as Planner + Implementer + Reviewer:

1. Create a short implementation plan.
2. Read only the task-specific docs from `docs/index.md`.
3. Implement the smallest safe change.
4. Run relevant build/test checks if possible.
5. Report:
   - what changed,
   - what was verified,
   - what could not be verified.
