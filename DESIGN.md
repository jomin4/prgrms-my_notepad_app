---
version: alpha
name: Quiet Ink — 로컬 AI 노트 디자인 시스템
description: >
  데스크톱 AI 노트 앱(AI는 NVIDIA NIM 무료 API로 구동)을 위한 "조용한 잉크" 디자인 언어.
  UI 크롬은 종이·잉크의 무채색으로 물러나고, 색은 뜻이 있을 때만 등장한다.
  보라(#5b4bd6)는 오직 AI의 행동에만, teal은 연결·정상과 1차 기능에, amber는 2차(예정)에 쓴다.
  타이포는 3계층 — 노트 본문/제목은 세리프(글쓰기의 감각), UI 크롬은 산세리프, 모델·상태·코드는 모노.
  시그니처 규칙: AI가 쓰거나 그린 모든 것은 '보라 흔적'(왼쪽 마진 틱 또는 밑줄 하이라이트)을 남겨,
  누가 썼는지 항상 시각적으로 드러난다. '설치 없이 무료로 바로 쓰는 가벼움'이 정체성의 축이다(노트는 로컬 저장).

colors:
  primary: "#5b4bd6"
  primary-hover: "#4d3ec2"
  primary-active: "#4234ad"
  primary-soft: "#ecebfb"
  on-primary: "#ffffff"
  local: "#0e9e77"
  local-soft: "#e2f4ee"
  local-ink: "#0b6e54"
  pending: "#c07d16"
  pending-soft: "#f8efdc"
  pending-ink: "#7d500f"
  success: "#0e9e77"
  warning: "#c07d16"
  error: "#d64545"
  error-soft: "#fbeaea"
  canvas: "#f6f6f4"
  surface: "#ffffff"
  surface-soft: "#f1f1ee"
  surface-inset: "#ecebe7"
  ink: "#1a1a1e"
  body: "#3a3a40"
  muted: "#6f6e75"
  faint: "#a3a2a9"
  hairline: "#e7e6e2"
  hairline-strong: "#d9d8d3"

colorsDark:
  primary: "#8f82f2"
  primary-hover: "#a099f5"
  primary-active: "#7d6ff0"
  primary-soft: "#272348"
  on-primary: "#ffffff"
  local: "#41c69c"
  local-soft: "#12332a"
  local-ink: "#7fdcc0"
  pending: "#e0a54a"
  pending-soft: "#382b13"
  pending-ink: "#f0c98a"
  success: "#41c69c"
  warning: "#e0a54a"
  error: "#ec6a6a"
  error-soft: "#3a1d1d"
  canvas: "#151418"
  surface: "#1d1c22"
  surface-soft: "#212028"
  surface-inset: "#26252e"
  ink: "#edecf0"
  body: "#c8c7ce"
  muted: "#918f99"
  faint: "#6a6872"
  hairline: "#2b2a33"
  hairline-strong: "#3a3944"

fonts:
  sans: "Inter, -apple-system, 'Segoe UI', Roboto, sans-serif"
  serif: "'Source Serif 4', 'Iowan Old Style', Georgia, serif"
  mono: "'JetBrains Mono', ui-monospace, 'SF Mono', Menlo, monospace"

typography:
  display-lg:
    fontFamily: "{fonts.serif}"
    fontSize: 32px
    fontWeight: 500
    lineHeight: 1.2
    letterSpacing: -0.4px
  display-md:
    fontFamily: "{fonts.serif}"
    fontSize: 24px
    fontWeight: 500
    lineHeight: 1.25
    letterSpacing: -0.2px
  note-title:
    fontFamily: "{fonts.serif}"
    fontSize: 22px
    fontWeight: 500
    lineHeight: 1.3
    letterSpacing: -0.2px
  note-body:
    fontFamily: "{fonts.serif}"
    fontSize: 16px
    fontWeight: 400
    lineHeight: 1.7
    letterSpacing: 0
  ui-lg:
    fontFamily: "{fonts.sans}"
    fontSize: 18px
    fontWeight: 500
    lineHeight: 1.4
    letterSpacing: 0
  ui-label:
    fontFamily: "{fonts.sans}"
    fontSize: 14px
    fontWeight: 500
    lineHeight: 1.5
    letterSpacing: 0
  ui-body:
    fontFamily: "{fonts.sans}"
    fontSize: 14px
    fontWeight: 400
    lineHeight: 1.5
    letterSpacing: 0
  caption:
    fontFamily: "{fonts.sans}"
    fontSize: 12px
    fontWeight: 500
    lineHeight: 1.4
    letterSpacing: 0
  caption-caps:
    fontFamily: "{fonts.sans}"
    fontSize: 11px
    fontWeight: 500
    lineHeight: 1.4
    letterSpacing: 0.6px
  mono:
    fontFamily: "{fonts.mono}"
    fontSize: 12.5px
    fontWeight: 400
    lineHeight: 1.5
    letterSpacing: 0
  button:
    fontFamily: "{fonts.sans}"
    fontSize: 13px
    fontWeight: 500
    lineHeight: 1
    letterSpacing: 0

rounded:
  xs: 4px
  sm: 6px
  md: 8px
  lg: 12px
  xl: 16px
  pill: 9999px

spacing:
  xxs: 2px
  xs: 4px
  sm: 8px
  md: 12px
  lg: 16px
  xl: 24px
  xxl: 32px
  section: 48px

elevation:
  flat: "none"
  raised: "0 1px 2px rgba(20,18,40,0.06)"
  panel: "0 8px 30px -18px rgba(20,18,40,0.35)"

components:
  app-window:
    backgroundColor: "{colors.surface}"
    borderColor: "{colors.hairline-strong}"
    rounded: "{rounded.lg}"
  title-bar:
    backgroundColor: "{colors.surface-soft}"
    height: 50px
    borderBottom: "{colors.hairline}"
  sidebar:
    backgroundColor: "{colors.surface-soft}"
    width: 246px
    borderRight: "{colors.hairline}"
  note-item:
    textColor: "{colors.ink}"
    typography: "{typography.ui-label}"
    padding: 8px 12px
  note-item-selected:
    backgroundColor: "{colors.surface}"
    borderLeft: "2px solid {colors.primary}"
  editor:
    backgroundColor: "{colors.surface}"
    titleTypography: "{typography.note-title}"
    bodyTypography: "{typography.note-body}"
  mode-switch:
    backgroundColor: "{colors.surface-soft}"
    activeBackground: "{colors.surface}"
    rounded: "{rounded.md}"
    typography: "{typography.caption}"
  button-primary:
    backgroundColor: "{colors.primary}"
    hoverBackground: "{colors.primary-hover}"
    activeBackground: "{colors.primary-active}"
    textColor: "{colors.on-primary}"
    typography: "{typography.button}"
    rounded: "{rounded.md}"
    padding: 9px 16px
    height: 36px
    note: "primary = AI/생성 액션 또는 주요 CTA. 화면당 1개만."
  button-secondary:
    backgroundColor: "{colors.surface}"
    borderColor: "{colors.hairline-strong}"
    textColor: "{colors.ink}"
    typography: "{typography.button}"
    rounded: "{rounded.md}"
    padding: 9px 16px
    height: 36px
  button-ghost:
    backgroundColor: "transparent"
    hoverBackground: "{colors.surface-soft}"
    textColor: "{colors.body}"
    typography: "{typography.button}"
  input:
    backgroundColor: "{colors.surface-inset}"
    borderColor: "{colors.hairline}"
    textColor: "{colors.ink}"
    placeholderColor: "{colors.faint}"
    typography: "{typography.ui-body}"
    rounded: "{rounded.md}"
    padding: 8px 10px
  badge-phase1:
    backgroundColor: "{colors.local-soft}"
    textColor: "{colors.local-ink}"
    typography: "{typography.caption}"
    rounded: "{rounded.pill}"
    padding: 2px 8px
  badge-phase2:
    backgroundColor: "{colors.pending-soft}"
    textColor: "{colors.pending-ink}"
    typography: "{typography.caption}"
    rounded: "{rounded.pill}"
    padding: 2px 8px
  badge-ai:
    backgroundColor: "{colors.primary-soft}"
    textColor: "{colors.primary-active}"
    typography: "{typography.caption}"
    rounded: "{rounded.pill}"
    padding: 2px 8px
  ai-panel:
    backgroundColor: "{colors.surface-soft}"
    width: 356px
    borderLeft: "{colors.hairline}"
  chat-bubble-user:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "14px 14px 4px 14px"
    typography: "{typography.ui-body}"
  chat-bubble-ai:
    backgroundColor: "transparent"
    textColor: "{colors.ink}"
    typography: "{typography.ui-body}"
  tool-chip-done:
    backgroundColor: "{colors.local-soft}"
    textColor: "{colors.local-ink}"
    borderColor: "{colors.local}"
    typography: "{typography.caption}"
    rounded: "{rounded.sm}"
    note: "실행 완료된 도구(1차)."
  tool-chip-soon:
    backgroundColor: "{colors.pending-soft}"
    textColor: "{colors.pending-ink}"
    borderColor: "{colors.pending}"
    typography: "{typography.caption}"
    rounded: "{rounded.sm}"
    note: "아직 준비 중인 도구(2차)."
  composer:
    backgroundColor: "{colors.surface}"
    borderColor: "{colors.hairline-strong}"
    rounded: "{rounded.lg}"
    padding: 7px 9px
  status-bar:
    backgroundColor: "{colors.surface-soft}"
    textColor: "{colors.local-ink}"
    typography: "{typography.caption}"
    borderTop: "{colors.hairline}"
  settings-group:
    backgroundColor: "{colors.surface}"
    borderColor: "{colors.hairline}"
    rounded: "12px"
    titleTypography: "{typography.ui-label}"
    padding: 18px 20px
  api-key-input:
    backgroundColor: "{colors.surface-inset}"
    textColor: "{colors.ink}"
    typography: "{typography.mono}"
    rounded: "{rounded.md}"
    note: "항상 마스킹(dots) + 눈 토글. 발급 링크·연결 테스트 동반. 값은 로그·화면·커밋에 노출 금지, OS 자격증명 저장소/암호화 저장."
  ai-mark:
    borderLeft: "2px solid {colors.primary}"
    highlightBackground: "{colors.primary-soft}"
    note: "시그니처. AI가 생성/편집한 텍스트·도형에 항상 부착한다."
---

# Quiet Ink — 로컬 AI 노트 디자인 시스템

## Overview

데스크톱 AI 노트 앱을 위한 디자인 언어. 목표는 **글에 집중하게 만드는 조용한 도구**다.
UI는 종이(canvas)와 잉크(ink)의 무채색으로 물러나고, 색은 *의미를 가질 때만* 나타난다.
세 가지 신호색만 존재한다 — **보라 = AI의 행동**, **teal = 연결·정상·1차 기능**, **amber = 2차(예정)**.

핵심 정서: 가벼움(설치·구독 없이 바로) · 집중(글쓰기 우선) · 협업(AI는 조용한 동료).

## Colors

### Brand & Accent
- `primary` `#5b4bd6` — **AI 전용 색**. AI가 하는 모든 행동(생성·편집·도구 실행)과 주요 CTA에만.
  일반 UI 강조에 남발하지 않는다. 이 색을 보면 "AI가 관여했다"는 뜻이어야 한다.
- 라이트에서 hover/active는 더 어둡게(`primary-hover`/`primary-active`), 다크에서는 더 밝게.

### Surface
- `canvas` = 앱 배경(종이), `surface` = 카드·편집기, `surface-soft` = 사이드바·패널, `surface-inset` = 입력 배경.
- 표면은 평평하게. 그라디언트·질감·네온 금지. 깊이는 0.5px 헤어라인과 아주 옅은 그림자로만.

### Text
- `ink`(본문 강조) > `body`(본문) > `muted`(보조) > `faint`(placeholder). 4계층을 지킨다.
- 색 위 텍스트는 같은 계열의 어두운 톤(`*-ink`)을 쓴다. 순수 검정/회색을 컬러 배경에 얹지 않는다.

### Semantic
- `success` = teal(= local), `warning` = amber(= pending), `error` = `#d64545`.
- 단계 색과 의미 색을 통일: 1차/성공/연결은 teal, 2차/경고/대기는 amber. 사용자가 색만 봐도 상태를 읽는다.

## Typography

### Font Family
- `sans` (Inter 계열) — UI 크롬 전부: 버튼, 라벨, 내비, 채팅.
- `serif` (Source Serif 계열) — **노트 본문과 제목**. 노트는 '글'이라는 감각을 준다.
- `mono` (JetBrains Mono 계열) — 모델명·연결 상태·코드·기술 값.

### Hierarchy
`display-lg/md` → `note-title` → `note-body`(세리프) · `ui-lg/label/body` · `caption`/`caption-caps` · `mono` · `button`.
두 굵기만 쓴다: 400 regular, 500 medium. 600/700은 쓰지 않는다(무겁다).

### Principles
- 본문은 한 줄 ~60자 이내. 노트 편집 영역은 `max-width` 로 가독폭을 잡는다.
- 문장형 대소문자(sentence case). Title Case·ALL CAPS 금지(라벨 포함). `caption-caps`만 예외적 레터스페이싱.

### Note on Font Substitutes
웹폰트 CDN 없이도 깨지지 않도록 각 스택에 시스템 폰트 폴백을 포함했다. KMP/Compose 구현 시
Inter·Source Serif·JetBrains Mono를 번들하되, 미탑재 환경에선 폴백으로 자연스럽게 대체되게 한다.

## Layout

### Spacing System
`xxs 2 · xs 4 · sm 8 · md 12 · lg 16 · xl 24 · xxl 32 · section 48`. 세로 리듬은 이 스케일만 사용.

### Grid & Container
- 데스크톱 기본: `사이드바(246) · 편집기(가변) · AI 패널(356)` 3열 + 상단바(50) + 하단 상태바.
- 최소 창 폭 ~980px. 좁아지면 AI 패널부터 접는다(아래 Responsive).

### Whitespace Philosophy
여백이 곧 컨테이너다. 카드 남발 대신 헤어라인과 간격으로 영역을 나눈다. "너무 복잡함"이 가장 흔한 실수 — 기본은 더 조용하게.

## Elevation & Depth
- `flat`(기본) · `raised`(작은 카드) · `panel`(떠 있는 패널/팝오버). 동시에 떠 있는 층은 최대 2개.
- 그림자는 기능적으로만. 장식용 글로우·블러 금지.

## Shapes

### Border Radius Scale
`xs 4 · sm 6 · md 8 · lg 12 · xl 16 · pill`. 컨트롤은 md, 카드/창은 lg, 뱃지/칩은 pill.
한쪽 테두리(예: 선택 표시 `border-left`)에는 라운드를 주지 않는다.

## Components

### Top Navigation (Title bar)
`surface-soft` 배경, 좌측 브랜드 + `mono`로 모델 상태(`llama3.1:8b · 연결됨`, teal 점), 우측 창 컨트롤.

### Buttons
- `button-primary`(보라) = AI/생성·주요 CTA, 화면당 1개. `button-secondary`(윤곽) = 일반. `button-ghost` = 저강도.
- 비활성 버튼은 지양. 눌렀을 때 반응으로 처리.

### Cards & Containers
`surface` 배경 + 0.5px `hairline` + `lg` 라운드. 밀집 리스트(노트 목록)는 카드가 아니라 테두리 행으로.

### Inputs & Forms
`surface-inset` 배경 + `hairline`. placeholder는 `faint`. 포커스는 `primary` 링(장식 그림자 금지).
- **API 키 입력**: 마스킹(dots) 기본 + 눈 토글, 발급 링크·연결 테스트 동반. 값은 절대 평문 노출 금지.

### Tags / Badges
`badge-phase1`(teal) · `badge-phase2`(amber) · `badge-ai`(보라). 단계·주체를 색으로 즉시 구분.

### Tool chips
AI 패널에서 도구 실행을 칩으로 표시: `tool-chip-done`(teal, 실행됨·1차) / `tool-chip-soon`(amber, 예정·2차).

### AI mark (시그니처)
AI가 생성/편집한 **모든 텍스트·도형**에 `ai-mark`를 부착한다 — 인라인은 `primary-soft` 밑줄 하이라이트,
블록은 왼쪽 `2px primary` 마진 틱. 사람이 쓴 잉크와 AI의 흔적이 항상 구별되어야 한다.

## Do's and Don'ts

### Do
- 색은 의미가 있을 때만. 보라는 AI, teal은 연결/1차, amber는 2차.
- 노트 본문은 세리프, UI는 산세리프, 상태·모델은 모노.
- AI 산출물엔 반드시 보라 흔적을 남긴다.
- API 키는 마스킹 + 눈 토글, "이 기기에 암호화 저장" 안내를 함께 둔다.
- 평평한 표면 + 0.5px 헤어라인 + 넉넉한 여백.

### Don't
- 보라를 일반 강조색으로 남발하지 않는다.
- 그라디언트·질감·네온·큰 그림자·이모지 금지.
- 600/700 굵기, Title Case, ALL CAPS 금지.
- 카드로 화면을 가득 채우지 않는다.
- API 키를 평문으로 표시·로그·커밋하지 않는다.

## Responsive Behavior

### Breakpoints
- `≥1200`: 3열 전개. `980–1199`: AI 패널을 오버레이/토글로. `<980`: 사이드바 접기(햄버거), 단일 컬럼.

### Touch Targets
데스크톱 우선이지만 클릭 타깃 최소 32px 높이 유지.

### Collapsing Strategy
좁아질 때 접는 순서: ① AI 패널(토글) → ② 사이드바(오버레이). 편집기는 항상 남긴다.

### Image / Canvas Behavior
2차 캔버스(그림) 모드는 편집기 영역을 그대로 차지. 캔버스 요소도 AI 생성이면 보라 흔적 규칙을 따른다.

## Iteration Guide
- 새 화면/컴포넌트는 먼저 이 토큰으로 조립한다. 토큰에 없는 값이 필요하면 여기 먼저 추가하고 쓴다.
- 색을 새로 만들기 전에 "이 색이 뜻하는 의미"를 정의한다. 의미 없는 색은 추가하지 않는다.
- 변경 시 `wiki/_meta/changelog.md`에 기록.

## Known Gaps
- 2차 캔버스(그림) 모드의 세부 컴포넌트(도형 팔레트, 핸들)는 미정 — 2차 설계에서 채운다.
- 실제 폰트 번들링·라이선스, 접근성 대비(AA) 정밀 검증은 구현 단계에서 확정.
- 온보딩/빈 상태(empty state) 화면 토큰은 추후 추가. (설정 페이지·API 키 입력은 정의됨)
