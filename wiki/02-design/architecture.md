---
title: 아키텍처 & NIM 연동 흐름
phase: 02-design
status: draft
updated: 2026-07-15
tags: [architecture, nim, mvvm]
---

## TL;DR
- 단일 데스크톱 앱(Compose) + 로컬 DB(SQLDelight) + 외부 **NVIDIA NIM API**. 계층은 MVVM.
- AI 요청 → 모델이 도구 호출 → 앱이 노트 수정·기록 → (최종 응답) → 화면 반영. 이 루프가 핵심.
- API 키는 보안 저장소에서만 읽어 요청 헤더에 싣는다(메모리 밖으로 새지 않게).

## 시스템 구성
```
[Compose UI] ──> [ViewModel(MVVM)] ──> [Repository]
                                         ├─> LocalStore  (SQLDelight / SQLite)  ← notes, tool_runs, ai_spans, messages
                                         ├─> SecureStore (OS 자격증명 저장소)     ← API 키
                                         └─> AiClient    (NVIDIA NIM, OpenAI 호환) ── 인터넷 ──> NIM
                                               └─ ToolExecutor (tool_calls → 노트 조작)
```
- **KMP 구성(대략)**: `commonMain`(도메인·데이터·뷰모델), `desktopMain`(Compose UI, SecureStore 구현).
- 외부 의존은 NIM API 하나. 노트 데이터는 전부 로컬.

## AI 연동 흐름 (도구 호출 루프)
1. **키 로드**: 앱 시작 시 SecureStore에서 API 키 로드. 없으면 설정 페이지로 유도.
2. **요청**: 사용자가 AI 패널에 입력 → 앱이 NIM `/v1/chat/completions`에 `messages` + `tools`(도구 스키마) + 현재 노트 컨텍스트 전송. 헤더 `Authorization: Bearer <키>`.
3. **판단**: 모델이 `tool_calls`(예: `append_note`) 반환.
4. **실행**: ToolExecutor가 해당 도구를 Repository로 실행 → 노트 본문 수정 + `tool_runs` 기록 + `ai_spans`(ai-mark) 저장.
5. **되돌림**: 도구 결과를 `tool` 메시지로 NIM에 다시 보내 자연어 최종 응답 수신(선택).
6. **반영**: 편집기에 수정 결과 + ai-mark 표시, AI 패널에 응답·도구 칩 표시.
7. **예외**: 실패/한도 초과 → 노트 미변경 + 안내(폴백). 오프라인 → AI 기능 비활성, 편집은 가능.

## 보안
- API 키는 SecureStore에서만 읽어 요청 시점에 헤더로 사용. 로그·화면·커밋·크래시 리포트에 노출 금지.
- 노트 본문이 요청에 포함되어 NIM으로 전송됨(사용자에게 명확히 고지).

## 다음
- [데이터 모델](data-model.md) · [도구 스키마](tools-spec.md).
- 03 개발: 이 설계대로 M1(노트 CRUD)부터 구현.
