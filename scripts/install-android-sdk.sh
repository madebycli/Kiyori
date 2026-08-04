#!/usr/bin/env bash
set -Eeuo pipefail

sdk_root="${ANDROID_SDK_ROOT:-${ANDROID_HOME:-}}"
if [[ -z "$sdk_root" ]]; then
  echo "ANDROID_SDK_ROOT or ANDROID_HOME must be set." >&2
  exit 1
fi

sdkmanager="$sdk_root/cmdline-tools/latest/bin/sdkmanager"
if [[ ! -x "$sdkmanager" ]]; then
  echo "sdkmanager not found at: $sdkmanager" >&2
  exit 2
fi

# License acceptance can end with SIGPIPE once sdkmanager has read enough input.
yes | "$sdkmanager" --licenses >/dev/null 2>&1 || true

packages_file="$(mktemp)"
trap 'rm -f "$packages_file"' EXIT
"$sdkmanager" --list --channel=3 > "$packages_file"

available_paths="$({
  awk -F '|' '
    /^[[:space:]]*(platforms;|build-tools;)/ {
      path = $1
      gsub(/^[[:space:]]+|[[:space:]]+$/, "", path)
      print path
    }
  ' "$packages_file"
} | sort -u)"

platform_package=""
for candidate in \
  "platforms;android-37" \
  "platforms;android-37.0" \
  "platforms;android-CinnamonBun"
do
  if printf '%s\n' "$available_paths" | grep -Fxq "$candidate"; then
    platform_package="$candidate"
    break
  fi
done

if [[ -z "$platform_package" ]]; then
  echo "No Android 17 SDK platform package was published to this runner." >&2
  echo "Available Android platform packages:" >&2
  printf '%s\n' "$available_paths" | grep '^platforms;' >&2 || true
  exit 3
fi

build_tools_package="$(
  printf '%s\n' "$available_paths" \
    | grep -E '^build-tools;37([.]|$)' \
    | sort -V \
    | tail -n 1
)"

if [[ -z "$build_tools_package" ]]; then
  echo "No Android Build Tools 37 package was published to this runner." >&2
  echo "Available Build Tools packages:" >&2
  printf '%s\n' "$available_paths" | grep '^build-tools;' | tail -n 20 >&2 || true
  exit 4
fi

printf 'Installing %s and %s\n' "$platform_package" "$build_tools_package"
"$sdkmanager" "$platform_package" "$build_tools_package" --channel=3

build_tools_version="${build_tools_package#build-tools;}"
test -d "$sdk_root/build-tools/$build_tools_version"

if [[ -n "${GITHUB_ENV:-}" ]]; then
  printf 'ANDROID_BUILD_TOOLS_VERSION=%s\n' "$build_tools_version" >> "$GITHUB_ENV"
  printf 'ANDROID_PLATFORM_PACKAGE=%s\n' "$platform_package" >> "$GITHUB_ENV"
fi

printf 'Android SDK ready: platform=%s build-tools=%s\n' \
  "$platform_package" "$build_tools_version"
