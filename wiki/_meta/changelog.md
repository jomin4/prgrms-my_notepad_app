---
title: 진행 로그 (changelog)
phase: _meta
status: in-progress
updated: 2026-07-15
tags: [changelog]
---

## TL;DR
- 날짜별 작업 내역. 최신이 위. (날짜 / 무엇을 / 왜 / 결과)

## 2026-07-15
- **AI 제공자 전환 — Ollama(로컬) → NVIDIA NIM(무료 API + 사용자 키)**: 설치 장벽 제거·효율 우선. 차별점을 '완전 로컬 프라이버시' → **'설치·구독 없이 무료로 바로'(노트는 로컬 소유)**로 재정의. PRD·DESIGN·화면·개요·용어·README·llms.txt 일괄 갱신. AI 추론은 클라우드로 나가되 노트 저장은 로컬 유지.
- **소스관리 세팅 + 원격 반영 자동화**: GitHub `prgrms-my_notepad_app`(Public) 생성·연결(기본 `main`). `.gitignore`, `scripts/sync-remote.sh`, `wiki/04-source-control/git-strategy.md` 추가. 규칙: **챕터 마무리마다 Claude가 자동으로 add→commit→push** (CLAUDE.md §6).
- **PRD 작성(1차 MVP)**: `wiki/01-planning/prd.md`. 타겟(프라이버시 중시 지식노동자), 성공기준, 범위(노트관리·Ollama연동·텍스트 작성 도구·ai-mark), 비범위(그림 캔버스는 2차·클라우드·모바일 제외), 마일스톤 M1~M4, 가정 3개(플랫폼 Windows우선·Ollama 직접설치·Pro 수익화) 확인 대기.
- **디자인 시스템 도입 — DESIGN.md 방식 채택**: getdesign.md의 DESIGN.md(Google Stitch) 포맷을 분석·적용. 우리 앱 고유 디자인 언어 **"Quiet Ink"**(무채색 크롬 + 보라=AI / teal=로컬·1차 / amber=2차, 세리프 노트본문·산세리프 UI·모노 상태, 시그니처=AI 보라 흔적)를 루트 `DESIGN.md`에 정식 포맷(프론트매터 토큰 + 본문 규칙)으로 저술. 앞으로 모든 UI 작업의 근거.
- **프로덕션 화면(v2)**: DESIGN.md(Quiet Ink) 토큰을 엄격히 적용한 출시 수준 목업으로 재정비 → `wiki/02-design/ui-mockup.html`(자체 완결형 HTML). 설명용 텍스트/뱃지 제거, 실제 앱 크롬만. 보라=AI 규칙 준수(선택 표시는 무채색), AI 작성분엔 ai-mark(보라 흔적).
- **화면 구성 목업(v1)**: 데스크톱 4영역 레이아웃(상단 Ollama 상태·좌측 노트목록·중앙 편집기·우측 AI 패널) HTML 목업 제작. 1차(텍스트 도구)/2차(캔버스) 색으로 구분.

- **진행 방식 확정 + 문서화**: `CLAUDE.md`(운영 원칙), 작업 사이클 시각화.
- **앱 컨셉 확정**: 로컬 LLM(Ollama)용 데스크톱 노트 앱 + 모델에게 쥐여줄 도구. 1차 텍스트 작성 도구 → 2차 excalidraw식 그림 도구.
- **llm-wiki 도입**: 재사용 가능한 단계별 문서 위키 스캐폴드 구축(이 `wiki/` 폴더).
- **위키 목적 확장**: 수동 README(아키텍처·시스템 구성·설치)를 대체하는 **질문 가능한 저장소**로. 외부 사용자가 자연어로 질문해 프로젝트를 즉시 파악.
- **쿼리 방식 결정**: "리포 문서 + 각자의 AI 도구". 인프라 없이 리포에 문서를 두고, 루트 `llms.txt`(AI용 지도) + `README.md`(사람용 안내판) 추가. 리포 복사만으로 재사용.
- **다음 할 일**: 1차 기준 기획서(PRD) 작성 → `01-planning/`에 저장. (또는 Ollama 도구호출 최소 프로토타입 선검증)
