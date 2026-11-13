#!/usr/bin/env python3
"""Basic repository checks; not a substitute for browser/device testing."""
from pathlib import Path
from html.parser import HTMLParser
import shutil
import subprocess
import sys
import tempfile

root = Path(__file__).resolve().parent.parent
source = root / 'index.html'
asset = root / 'android/app/src/main/assets/index.html'
if not source.is_file() or not asset.is_file():
    sys.exit('ERROR: web source or Android asset is missing.')
if source.read_bytes() != asset.read_bytes():
    sys.exit('ERROR: Android asset differs; run python3 scripts/sync_android.py')

class Scripts(HTMLParser):
    def __init__(self):
        super().__init__(convert_charrefs=False)
        self.in_script = False
        self.items = []
        self.current = []
    def handle_starttag(self, tag, attrs):
        if tag == 'script':
            if dict(attrs).get('src'):
                sys.exit('ERROR: external script source in offline game')
            self.in_script = True
            self.current = []
    def handle_data(self, data):
        if self.in_script:
            self.current.append(data)
    def handle_endtag(self, tag):
        if tag == 'script' and self.in_script:
            self.items.append(''.join(self.current))
            self.in_script = False

parser = Scripts()
parser.feed(source.read_text(encoding='utf-8'))
node = shutil.which('node')
if not node:
    sys.exit('ERROR: Node.js is needed for JS syntax checking (not to run the game).')
if not parser.items:
    sys.exit('ERROR: no scripts found.')
with tempfile.TemporaryDirectory() as tmp:
    for i, script in enumerate(parser.items):
        path = Path(tmp) / f'script-{i}.js'
        path.write_text(script, encoding='utf-8')
        result = subprocess.run([node, '--check', str(path)], check=False)
        if result.returncode:
            sys.exit(f'ERROR: script {i} failed JavaScript syntax check.')
print(f'OK: Android asset matches; {len(parser.items)} inline scripts pass syntax checks.')
