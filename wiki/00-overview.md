---
title: 프로젝트 전체 개요
phase: 00-overview
status: in-progress
updated: 2026-07-15
tags: [overview]
---

## TL;DR
- AI가 노트를 직접 조작하는 **데스크톱 노트 앱**. AI는 **NVIDIA NIM 무료 API**(사용자 본인 키)로 구동.
- 1차: 텍스트 작성 도구 → 2차: excalidraw식 그림 그리기 도구.
- **설치·구독 없이 무료로 바로**, AI가 직접 노트 조작, 노트는 로컬 파일로 소유가 차별점.

## 비전
사용자가 질문하면 AI가 노트에 직접 글을 쓰고, 다이어그램까지 그려주는 **가볍게 바로 쓰는 AI 노트 앱**.

## 기술 방향 (권장안)
- Kotlin Multiplatform + Compose Multiplatform (Desktop / JVM)
- 로컬 저장: SQLDelight + SQLite (초기엔 파일 기반 가능)
- AI: NVIDIA NIM (OpenAI 호환 API, `integrate.api.nvidia.com/v1`) + 사용자 API 키, tool calling

## 진행 파이프라인
기획 → 설계 → 개발 → 소스관리 → 배포 → 출시 → 유지보수 → 수익화
(각 단계 산출물은 이 위키의 해당 폴더에 보관)

## 현재 현황
- [x] 진행 방식·앱 컨셉 확정
- [x] llm-wiki 스캐폴드 구축
- [x] 디자인 시스템(DESIGN.md · Quiet Ink) + 프로덕션 화면 목업
- [x] 기획서(PRD 1차 MVP) 작성 — 가정 확인 대기
- [x] 소스관리 세팅(GitHub prgrms-my_notepad_app) + 원격 반영 자동화
- [x] 방향 결정(Windows·설정 온보딩·수익화) + 설정 페이지 설계
- [x] 설계 v1(데이터 모델·도구 스키마·아키텍처/NIM 흐름)
- [x] 개발 M1.1 — KMP/Compose 스캐폴드 + 첫 실행 창
- [x] 개발 M1.2 — 노트 UI(목록·편집기·검색·삭제, Quiet Ink)
- [ ] 개발 M1.3 — 로컬 저장(SQLDelight) ← 다음
- 상세: [진행 로그](_meta/changelog.md)
