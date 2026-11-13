# Project notes for coding agents

## Start here

This repository is a self-contained, offline, bilingual Persian/English Ludo-like game. `index.html` at the repository root is the source of truth for the game (inline CSS, application code, vendor code, fonts and permitted artwork). The Android app in `android/` hosts a byte-identical copy in a local WebView.

Read `README.md` and inspect the existing code before making changes. Maintain existing user-facing Persian and English behavior, offline operation, responsive portrait/landscape UI, saved game compatibility, and existing progression/unlocks unless the user explicitly changes them. Avoid rewriting or regex-patching the embedded vendor libraries. Back up before substantial edits. Use procedural game assets except existing permitted inline artwork. Respect the existing GPU and animation trade-offs.

After changing root `index.html`, run:

```sh
python3 scripts/sync_android.py
python3 scripts/check_project.py
```

The check only performs basic JS syntax and asset-sync checks. For behavior changes run meaningful browser tests for affected flows, narrow/mobile and landscape, both languages, saved data and Android WebView when relevant. Report honestly what you did not test. Do not claim a new APK exists unless you built it; release requires the owner's signing key.

## Security and privacy

Never commit a key generator, RSA private keys, signing keystores, license codes, secrets or test user data. Offline short-code verification in the current client contains a recoverable verification secret and is forgeable. It is a convenience mechanism, not a secure payment/license system. A public repository or distributed app makes it easier to bypass. Legacy RSA verification uses a public key; do not replace it without preserving existing licenses or communicating a migration. See README.

Do not assume any app, remote, credentials, deployment or GitHub repository is connected. Ask before publishing or changing remotes. Do not overwrite third-party licenses, insert secret data, or infer a repository license.
