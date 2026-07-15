---
title: 도구 스키마 (AI tool calling)
phase: 02-design
status: draft
updated: 2026-07-15
tags: [tool-calling, nim, api]
---

## TL;DR
- AI에게 "노트를 조작하는 도구"를 함수 스키마로 준다. 모델이 도구를 골라 호출하면 **앱이 실제로 노트를 고친다**.
- 1차(텍스트) 도구 4개: `append_note` · `insert_note` · `replace_selection` · `rewrite_note`.
- 모든 실행은 `tool_runs`에 기록하고, 새로 들어간 텍스트 범위는 `ai_spans`에 남겨 **ai-mark**로 표시.

## 방식
NVIDIA NIM은 OpenAI 호환이라 표준 **tool calling**을 쓴다. 앱이 요청에 `tools`(함수 스키마)를 실어 보내면,
모델은 답 대신 `tool_calls`(함수명 + 인자)를 반환한다. 앱이 그 함수를 실행하고 결과를 되돌려준다.

## 1차 도구 (텍스트) — 스키마
```json
[
  {
    "type": "function",
    "function": {
      "name": "append_note",
      "description": "현재 노트 본문 끝에 텍스트를 이어 붙인다.",
      "parameters": {
        "type": "object",
        "properties": { "text": { "type": "string", "description": "추가할 텍스트(마크다운 허용)" } },
        "required": ["text"]
      }
    }
  },
  {
    "type": "function",
    "function": {
      "name": "insert_note",
      "description": "현재 커서 위치(또는 지정 위치)에 텍스트를 삽입한다.",
      "parameters": {
        "type": "object",
        "properties": {
          "text": { "type": "string" },
          "position": { "type": "integer", "description": "본문 내 삽입 오프셋. 생략 시 커서 위치" }
        },
        "required": ["text"]
      }
    }
  },
  {
    "type": "function",
    "function": {
      "name": "replace_selection",
      "description": "사용자가 선택한 영역을 새 텍스트로 교체한다.",
      "parameters": {
        "type": "object",
        "properties": { "text": { "type": "string" } },
        "required": ["text"]
      }
    }
  },
  {
    "type": "function",
    "function": {
      "name": "rewrite_note",
      "description": "노트 본문 전체를 새 텍스트로 교체한다(개요 작성·전체 정리 등).",
      "parameters": {
        "type": "object",
        "properties": { "text": { "type": "string" } },
        "required": ["text"]
      }
    }
  }
]
```

## 도구 선택 (사용자 제어)
- **설정 → 도구**: 도구를 전역 on/off (커넥터식 허용 목록).
- **챗 입력 `+`**: 이번 대화에서 쓸 도구를 골라 **그 도구만 강제**(`tool_choice`로 특정 function 지정). 미선택이면 `auto`.

## 실행 규칙
- 도구 실행 = Repository를 통해 노트 본문 변경 → `tool_runs` 기록 → 삽입/교체된 범위를 `ai_spans`에 저장(ai-mark).
- 도구가 없어도 되는 질문은 일반 텍스트로 답한다(도구 강제 아님).
- **실패/한도 초과** 시: 노트를 건드리지 않고 사용자에게 안내(폴백).
- 컨텍스트: 요청 시 현재 노트 제목·본문(필요 시 선택 영역)을 시스템/유저 메시지에 포함.

## 2차 도구 (그림) — 범위 밖(예정)
- `draw_diagram({ elements: [...] })` — excalidraw 유사 요소(사각형·화살표·텍스트·위치)를 캔버스에 렌더링.
- 스키마·요소 규격은 2차 설계에서 확정. 생성물은 ai-mark 규칙을 따른다.

## 다음
- [데이터 모델](data-model.md) · [아키텍처/연동 흐름](architecture.md).
