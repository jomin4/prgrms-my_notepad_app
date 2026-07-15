---
title: 데이터 모델 (로컬 저장)
phase: 02-design
status: draft
updated: 2026-07-15
tags: [data, erd, sqldelight]
---

## TL;DR
- 로컬 SQLite(SQLDelight)에 **노트**를 저장하고, **AI 도구 실행 기록**과 **AI가 쓴 범위**(ai-mark용)를 함께 남긴다.
- **API 키는 DB에 넣지 않는다** — OS 자격증명 저장소(암호화)에 별도 보관.
- 표는 4개: `notes` · `tool_runs` · `ai_spans` · `messages`.

## 엔티티

### notes — 노트
| 필드 | 타입 | 설명 |
|---|---|---|
| id | INTEGER PK | 노트 식별자 |
| title | TEXT | 제목 |
| body | TEXT | 본문(플레인/마크다운) |
| created_at | INTEGER | 생성 시각(epoch ms) |
| updated_at | INTEGER | 수정 시각 |

### tool_runs — AI 도구 실행 기록
| 필드 | 타입 | 설명 |
|---|---|---|
| id | INTEGER PK | |
| note_id | INTEGER FK→notes | 대상 노트 |
| tool | TEXT | 도구명(append_note 등) |
| args_json | TEXT | 호출 인자(JSON) |
| status | TEXT | success / failed |
| created_at | INTEGER | |

### ai_spans — AI가 쓴 텍스트 범위 (ai-mark 렌더링용)
| 필드 | 타입 | 설명 |
|---|---|---|
| id | INTEGER PK | |
| note_id | INTEGER FK→notes | |
| tool_run_id | INTEGER FK→tool_runs | 어느 실행이 남긴 흔적인지 |
| start_offset | INTEGER | 본문 내 시작 위치 |
| end_offset | INTEGER | 끝 위치 |
| created_at | INTEGER | |

### messages — AI 패널 대화 (노트별)
| 필드 | 타입 | 설명 |
|---|---|---|
| id | INTEGER PK | |
| note_id | INTEGER FK→notes (nullable) | |
| role | TEXT | user / assistant / tool |
| content | TEXT | 메시지 본문 |
| tool_run_id | INTEGER FK→tool_runs (nullable) | 도구 호출 연결 |
| created_at | INTEGER | |

## 관계
- `notes` **1 : N** `tool_runs`
- `notes` **1 : N** `ai_spans`
- `tool_runs` **1 : N** `ai_spans`
- `notes` **1 : N** `messages`

## 설정/보안 저장 (DB 아님)
- **API 키**: Windows 자격증명 저장소(DPAPI) 등 OS 보안 저장소에 암호화 보관. 화면·로그·커밋 노출 금지.
- **모델명·테마 등 환경설정**: 간단한 prefs 파일(또는 DataStore). 민감정보 아님.

## 다음
- 이 모델 위에서 동작하는 [도구 스키마](tools-spec.md) · [아키텍처/연동 흐름](architecture.md).
