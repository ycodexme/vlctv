#!/bin/bash

echo "=== Script de Debug IPTV VLC Android ==="
echo ""
echo "1. Nettoyage des logs précédents..."
adb logcat -c

echo "2. Lancement de l'application..."
adb shell am start -n org.videolan.vlc.debug/org.videolan.vlc.StartActivity

echo ""
echo "3. Capture des logs en temps réel..."
echo "   Maintenant:"
echo "   - Utilisez l'application"
echo "   - Cliquez sur une chaîne"
echo "   - Appuyez sur Ctrl+C pour arrêter"
echo ""
sleep 2

# Capture avec filtres simples
adb logcat | grep -E "(IPTV|VideoPlayer|VLC.*error|FATAL|AndroidRuntime)"
