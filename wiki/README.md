# 📚 llm-wiki

> 바이브 코딩 프로젝트의 **단계별 MD 산출물**을 보관하고, 사용자가 궁금할 때 **빠르게 답을 얻는** 지식베이스.
> 이 위키는 **재사용 가능한 껍데기(①층)** + **프로젝트별 내용(②층)** 으로 이루어진다.
> 새 프로젝트를 시작하면 이 `wiki/` 폴더 구조·`_templates/`·규칙을 그대로 복사해 재사용한다.

---

## 1. 사용법

**프로젝트 소유자**: 궁금한 게 생기면 그냥 물어보세요. 예: *"도구 호출 어떻게 하기로 했지?"*, *"배포는 어디까지 진행됐어?"*
Claude는 이 위키의 **인덱스(§4)** 에서 관련 문서를 찾아 → 각 문서의 **TL;DR** 로 즉답하고, 필요하면 깊이 설명합니다.

**외부 사용자**(팀원·신규 기여자): 이 저장소는 **질문 가능한 문서**입니다. README를 다 읽는 대신, 자신의 AI 도구(Claude Code, Cursor 등)로 리포를 열고 자연어로 물어보세요.
리포 루트의 [`llms.txt`](../llms.txt)가 AI에게 문서 지도를 제공하고, [`README.md`](../README.md)가 사람용 안내판입니다.

> 모든 문서는 맨 위 **TL;DR 3줄**이 강제라, 전문을 안 읽어도 빠르게 파악됩니다.

## 2. 폴더 구조 (재사용 껍데기 ①층)

```
wiki/
  README.md            ← 이 파일 (사용법 + 인덱스 라우터)
  00-overview.md       ← 프로젝트 한눈에
  01-planning/         ← 기획
  02-design/           ← 설계
  03-development/      ← 개발
  04-source-control/   ← 소스관리
  05-deployment/       ← 배포
  06-release/          ← 출시
  07-maintenance/      ← 유지보수
  08-monetization/     ← 수익화
  _templates/          ← 빈 문서 양식 (복사해서 채움)
  _meta/               ← glossary(용어), changelog(진행 로그)
```

각 단계 폴더는 그 단계에 무엇이 들어가는지 설명하는 `_about.md`를 가진다.

## 3. 문서 작성 규칙 (재사용 표준)

모든 문서는 아래 프론트매터 + TL;DR로 시작한다. 양식은 [_templates/_doc-template.md](_templates/_doc-template.md).

```yaml
---
title: 문서 제목
phase: 01-planning        # 소속 단계
status: draft             # draft | in-progress | done
updated: 2026-07-15       # 마지막 수정일 (절대날짜)
tags: []
---
```

- 파일/폴더명은 **프로젝트 이름을 넣지 않고 단계 번호 기반**으로 → 어느 프로젝트든 그대로 재사용.
- 새 문서를 만들면 아래 §4 인덱스에 **한 줄** 추가한다.

## 4. 인덱스 라우터 (질문 → 문서 매핑)

> 새 문서가 생길 때마다 한 줄씩 추가. Claude는 여기서 관련 문서를 먼저 찾는다.

- [00 전체 개요](00-overview.md) — 프로젝트 비전·현황 한눈에
- [DESIGN.md](../DESIGN.md) — UI 디자인 시스템(Quiet Ink). 모든 화면 작업의 근거
- [시작하기](getting-started.md) — 설치·실행 세팅 (신규 사용자 진입점)
- [01 기획 안내](01-planning/_about.md) — MVP·타겟·차별화가 들어가는 곳
  - [PRD (1차 MVP)](01-planning/prd.md) — 범위·타겟·성공 기준·마일스톤
- [02 설계 안내](02-design/_about.md) — 화면·데이터모델·아키텍처
  - [화면 구성](02-design/screens.md) — 4영역 + 설정 · 목업 [메인](02-design/ui-mockup.html) · [설정](02-design/settings-mockup.html)
  - [데이터 모델](02-design/data-model.md) — 로컬 DB 테이블(notes·tool_runs·ai_spans·messages)
  - [도구 스키마](02-design/tools-spec.md) — AI tool calling 함수 정의(1차 텍스트 4종)
  - [아키텍처 & NIM 흐름](02-design/architecture.md) — 계층·도구 호출 루프·보안
- [03 개발 안내](03-development/_about.md) — 기능 명세·기술 결정(ADR)
- [04 소스관리 안내](04-source-control/_about.md) — git·브랜치 전략
  - [Git 전략 & 원격 자동화](04-source-control/git-strategy.md) — 챕터마다 자동 push
- [05 배포 안내](05-deployment/_about.md) — 설치파일 빌드·릴리스
- [06 출시 안내](06-release/_about.md) — 배포 채널·랜딩페이지
- [07 유지보수 안내](07-maintenance/_about.md) — 버그·기능개선·장애
- [08 수익화 안내](08-monetization/_about.md) — 라이선스·프리미엄
- [용어사전](_meta/glossary.md) — 비개발자용 용어 풀이
- [진행 로그](_meta/changelog.md) — 날짜별 작업 내역
