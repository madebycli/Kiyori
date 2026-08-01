{ pkgs ? import <nixpkgs> {
    config = {
      allowUnfree = true;
      android_sdk.accept_license = true;
    };
  }
}:

let
  buildToolsVersion = "36.0.0";
  androidComposition = pkgs.androidenv.composeAndroidPackages {
    platformVersions = [ "37" ];
    buildToolsVersions = [ buildToolsVersion ];
    includeEmulator = false;
    includeSystemImages = false;
    includeSources = false;
    includeCmake = false;
  };
  androidSdk = androidComposition.androidsdk;
  androidHome = "${androidSdk}/libexec/android-sdk";
in
pkgs.mkShell {
  packages = with pkgs; [
    androidSdk
    jdk17
    git
    gh
    openssl
    coreutils
    findutils
    gnugrep
    gnused
    unzip
    zip
  ];

  JAVA_HOME = pkgs.jdk17.home;
  ANDROID_HOME = androidHome;
  ANDROID_SDK_ROOT = androidHome;

  # Android's Maven aapt2 binary is not usable directly on NixOS. Use the
  # aapt2 binary supplied by the composed Android SDK instead.
  GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidHome}/build-tools/${buildToolsVersion}/aapt2";

  shellHook = ''
    export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/${buildToolsVersion}:$PATH"
    printf 'sdk.dir=%s\n' "$ANDROID_HOME" > local.properties

    echo "Navori Android build shell"
    echo "Java:       $(java -version 2>&1 | head -n 1)"
    echo "Android SDK: $ANDROID_HOME"
    echo "Build:      ./gradlew :app:assembleFossDebug --no-daemon"
  '';
}
