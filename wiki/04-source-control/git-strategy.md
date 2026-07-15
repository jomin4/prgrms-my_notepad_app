---
title: Git 소스관리 & 원격 반영 자동화
phase: 04-source-control
status: in-progress
updated: 2026-07-15
tags: [git, automation]
---

## TL;DR
- 원격: GitHub `prgrms-my_notepad_app` (Public), 기본 브랜치 `main`.
- **챕터가 마무리될 때마다 Claude가 자동으로 원격 반영**(add → commit → push). 사용자는 학습에 집중.
- 수동으로 반영하려면: `./scripts/sync-remote.sh "커밋 메시지"`.

## 브랜치 전략
- 지금은 단순하게 `main` 직접 반영(바이브 코딩·1인 개발). 기능이 커지면 `feature/*` 도입.

## 원격 반영 자동화 (핵심)
- **트리거**: 하나의 챕터(작업 단위)가 끝날 때.
- **동작**: Claude가 `git add -A → git commit → git push`를 수행. 커밋 메시지는 그 챕터에서 한 일을 요약.
- **변경 없음이면** 커밋/푸시를 생략한다.
- **커밋 메시지 규칙**: `type: 요약` (예: `docs: PRD 1차 MVP 작성`, `feat: 노트 CRUD`). 문서 위주면 `docs:`.

## 수동 명령 (필요 시)
```bash
./scripts/sync-remote.sh "docs: 화면 설계 v2"
```

## 관련
- 운영 규칙: 루트 [CLAUDE.md](../../CLAUDE.md) §"소스관리 자동화".
- 진행 로그: [changelog](../_meta/changelog.md).
