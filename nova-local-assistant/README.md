# Nova Local Assistant

> **Type:** Local AI assistant and Python automation project  
> **Platform:** Windows  
> **Status:** Multi version personal project

Nova started as a command line helper and kept growing. Over time I added local model calls, voice input, system diagnostics, Windows automation, research tools, local memory experiments, and a visual workspace.

The full local source contains machine specific paths and personal settings, so I do not publish it as one giant file. I keep the public page focused on what I can verify and safely share.

## What I built

### Local model integration

Nova can send prompts to a local Ollama model and use the response inside other workflows.

### Voice

The project includes microphone input, speech recognition, text to speech, and typed fallback input.

### Windows automation

I worked with tools such as:

* PyAutoGUI
* Win32 COM
* UI Automation
* pywinauto
* clipboard access
* active window detection

The goal was to let Nova interact with normal desktop tasks rather than only answer text prompts.

### System diagnostics

The local versions inspect CPU use, memory, storage, battery status, running processes, and active windows.

A sanitized example is here: [system_diagnostics.py](examples/system_diagnostics.py).

### Research mode

Later versions added research workflows for source collection, page extraction, source quality checks, claim comparison, timelines, numeric evidence, and saved reports.

### Local memory

I experimented with JSON memory and optional vector search using ChromaDB and sentence transformers.

### Visual workspace

I also tested Tkinter and browser based visual interfaces, including Three.js and GLB model loading.

## Evidence I checked

I still have multiple Nova Python versions in my retained files. They include real functions for:

* local system diagnosis with psutil
* Windows and application control
* source collection and research
* engineering topic routing
* ESP32, I2C, SPI, UART, MPU6050, Raspberry Pi, and robotics source selection
* voice controls
* local reports and saved output

The public `examples/system_diagnostics.py` file is a sanitized representative sample, not a claim that the entire private Nova source is published here.

## Main libraries I used or tested

Python, Ollama, requests, BeautifulSoup, trafilatura, Playwright, SpeechRecognition, edge TTS, pyttsx3, psutil, PyAutoGUI, pyperclip, Win32 COM, pywinauto, ChromaDB, sentence transformers, Three.js, and GLB assets.

## Security notes

A local assistant that can control the desktop needs careful boundaries. The areas I would lock down most before treating Nova like a production tool are command execution, clipboard access, browser automation, local memory, untrusted web content, and prompt injection.
