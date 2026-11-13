#!/usr/bin/env python3
"""Copy the canonical web game into the Android WebView assets."""
from pathlib import Path
import shutil

root = Path(__file__).resolve().parent.parent
source = root / 'index.html'
target = root / 'android/app/src/main/assets/index.html'
target.parent.mkdir(parents=True, exist_ok=True)
shutil.copyfile(source, target)
print(f'Synced {source.relative_to(root)} -> {target.relative_to(root)}')
