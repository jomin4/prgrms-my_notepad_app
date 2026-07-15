---
title: 프로젝트 전체 개요
phase: 00-overview
status: in-progress
updated: 2026-07-15
tags: [overview]
---

## TL;DR
- 로컬 LLM(Ollama)을 위한 **데스크톱 노트 앱**. 모델에게 노트를 조작할 **도구**를 쥐여준다.
- 1차: 텍스트 작성 도구 → 2차: excalidraw식 그림 그리기 도구.
- 완전 로컬(프라이버시), 글+그림 둘 다, Ollama만으로 동작이 차별점.

## 비전
사용자가 질문하면 로컬 AI가 노트에 직접 글을 쓰고, 다이어그램까지 그려주는 **프라이빗 AI 노트 앱**.

## 기술 방향 (권장안)
- Kotlin Multiplatform + Compose Multiplatform (Desktop / JVM)
- 로컬 저장: SQLDelight + SQLite (초기엔 파일 기반 가능)
- AI: Ollama 로컬 모델 (`/api/chat` tool calling)

## 진행 파이프라인
기획 → 설계 → 개발 → 소스관리 → 배포 → 출시 → 유지보수 → 수익화
(각 단계 산출물은 이 위키의 해당 폴더에 보관)

## 현재 현황
- [x] 진행 방식·앱 컨셉 확정
- [x] llm-wiki 스캐폴드 구축
- [x] 디자인 시스템(DESIGN.md · Quiet Ink) + 프로덕션 화면 목업
- [x] 기획서(PRD 1차 MVP) 작성 — 가정 확인 대기
- [x] 소스관리 세팅(GitHub prgrms-my_notepad_app) + 원격 반영 자동화
- [ ] 설계(데이터 모델·도구 스키마) ← 다음
- 상세: [진행 로그](_meta/changelog.md)
