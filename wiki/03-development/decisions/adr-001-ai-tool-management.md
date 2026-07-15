---
title: ADR-001 AI 도구 관리 방식 — 지금은 in-app, MCP/LangGraph 보류
phase: 03-development
status: accepted
updated: 2026-07-15
tags: [decision, tools, mcp, langgraph, architecture]
---

## TL;DR
- 무엇: AI 도구를 **앱 내(Kotlin)에서 직접 정의·실행**하는 현재 방식을 유지. **LangGraph·MCP는 지금 도입하지 않음.**
- 왜: 도구 2개·단일 호출 루프엔 프레임워크가 오버스펙. LangGraph(Python/JS)는 데스크톱 번들을 무겁게 만들어 "설치 없이 가볍게" 정체성과 충돌. (YAGNI)

## 배경
M2.3에서 AI가 `append_note`/`rewrite_note` 도구를 호출해 노트를 직접 수정하는 기능이 동작한다.
도구 정의·실행은 앱 코드(`NimClient.noteTools`, `AiPanel.handleResult`)에 in-app으로 있다.
확장성(도구 관리·멀티스텝 에이전트)을 위해 **LangGraph 프레임워크 + MCP 서버** 도입을 검토했다.

## 선택지
- (A) 현행 유지: 앱 내 Kotlin 도구 호출 루프.
- (B) MCP 서버로 도구 분리·관리 (공식 Kotlin SDK 존재 → JVM 내 가능).
- (C) LangGraph로 에이전트 오케스트레이션 (Python/JS 전용).

## 결정
- **(A) 유지.** LangGraph·MCP 모두 지금은 미도입.
- 근거:
  - 현재 필요한 건 **단일 도구 호출**이지 멀티에이전트 그래프가 아님.
  - **LangGraph는 Python/JS 전용** → Kotlin 데스크톱에 붙이려면 별도 런타임(사이드카) 번들 필요 → `.msi` 비대·"가볍게 바로" 정체성 충돌.
  - 도구 2개엔 MCP 프로토콜 서버도 오버스펙.

## 재검토 트리거 (이때 다시 본다)
- **도구가 3~4개+ 로 늘면** (2차 그림·검색·파일 등) → **Kotlin MCP**(공식 SDK, JVM 유지)로 도구 계층 분리 검토.
- **멀티스텝/멀티에이전트**(예: 조사→개요→작성→그림)가 실제 필요해지면 → **JVM 오케스트레이션(LangChain4j)** 또는 명확히 스코프된 로컬 사이드카. (LangGraph는 그때도 번들 부담을 저울질)

## 결과 / 영향
- 지금 코드 그대로 진행. 개발 속도·가벼움 유지.
- 확장 시 첫 단계는 **`ToolRegistry` 추상화**(도구 = 이름·스키마·실행 인터페이스)로 경계를 열고, 그 뒤 MCP 클라이언트를 한 구현체로 끼우는 순서.

## 관련
- [도구 스키마](../../02-design/tools-spec.md) · [아키텍처](../../02-design/architecture.md)
