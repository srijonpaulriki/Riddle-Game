@echo off
title Riddle Quest

powershell.exe -NoProfile -NoExit -Command "$Host.UI.RawUI.WindowTitle = 'Riddle Quest'; Set-Location -LiteralPath '%~dp0src'; chcp 65001 > $null; $files = @(Get-ChildItem -Recurse -Filter '*.java' | ForEach-Object FullName); if ($files.Count -eq 0) { Write-Host 'No Java files found!' -ForegroundColor Red } else { javac -encoding UTF-8 -d . $files; if ($LASTEXITCODE -eq 0) { java '-Dfile.encoding=UTF-8' '-Driddle.ansi=true' -cp . Srijon.RiddleGame } }"