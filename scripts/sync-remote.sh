#!/usr/bin/env bash
# 챕터 마무리 시 원격 반영 자동화.
# 사용: ./scripts/sync-remote.sh "커밋 메시지"
# 변경이 없으면 아무것도 하지 않는다.
set -e

msg="${1:-chore: sync progress}"

git add -A
if git diff --cached --quiet; then
  echo "변경 없음 — 커밋/푸시 생략"
  exit 0
fi

git commit -m "$msg"
git push
echo "✓ 원격 반영 완료: $msg"
