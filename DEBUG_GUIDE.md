# 🐛 Guide de Debug - VLC Android IPTV

## Problèmes Rencontrés

**Symptômes:**
- ✗ Écran figé pendant la lecture
- ✗ Compteur de minutes continue mais image freeze
- ✗ Le lecteur se ferme parfois

---

## 📋 Checklist de Diagnostic

### 1. Vérifier l'installation

```bash
# Vérifier que l'APK est installé
adb shell pm list packages | grep vlc

# Version installée
adb shell dumpsys package org.videolan.vlc.debug | grep versionName
```

### 2. Lancer le Script de Debug

```bash
cd /Users/m1/Documents/vlc-android
./debug_iptv.sh
```

Ensuite:
1. Utilisez l'application
2. Cliquez sur une chaîne
3. Notez ce qui se passe
4. Les logs s'afficheront en temps réel

### 3. Captures de Logs Spécifiques

#### Logs IPTV seulement
```bash
adb logcat | grep "IPTV"
```

#### Logs VideoPlayer
```bash
adb logcat | grep "VideoPlayer"
```

#### Tous les logs VLC
```bash
adb logcat | grep "VLC"
```

#### Erreurs critiques
```bash
adb logcat | grep -E "(FATAL|ERROR|AndroidRuntime)"
```

---

## 🔍 Causes Possibles

### 1. **URLs M3U8 Invalides**

**Symptôme:** Freeze immédiat ou écran noir

**Solution:**
- Testez l'URL manuellement:
```bash
curl -I "https://iptv.vdfr.uk/99e312f7/proxy_user/proxy_password/xxxxx/xxxxxx.m3u8"
```

- Si erreur 403/401: URLs avec authentification requise
- Si timeout: Serveur IPTV down

**Fix:** Utiliser une autre source IPTV ou ajouter headers

### 2. **Permissions Réseau**

**Vérifier:**
```bash
adb shell dumpsys package org.videolan.vlc.debug | grep permission
```

**Doit contenir:**
- `android.permission.INTERNET: granted=true`
- `android.permission.ACCESS_NETWORK_STATE: granted=true`

### 3. **Problème de Codec/Format**

**Symptôme:** Audio joue mais pas la vidéo (ou inverse)

**Logs à chercher:**
```
E/VLC: codec not found
E/VLC: no suitable decoder module
```

**Solution:** Le stream peut nécessiter un codec spécifique

### 4. **Mémoire Insuffisante**

**Vérifier:**
```bash
adb shell dumpsys meminfo org.videolan.vlc.debug
```

**Si mémoire basse:**
- Redémarrer l'émulateur
- Augmenter la RAM de l'émulateur

### 5. **Problèmes d'Émulateur**

**Symptômes spécifiques émulateur:**
- Audio I/O errors (normal sur émulateur)
- Freeze graphique (limitation GPU)

**Solutions:**
1. Tester sur un appareil réel
2. Activer l'accélération matérielle (AVD Manager)
3. Utiliser un émulateur plus performant

---

## 🛠️ Solutions par Symptôme

### ❌ Écran Figé mais Compteur Continue

**Cause probable:** Problème de rendu vidéo

**Test:**
1. Vérifier les logs VLC:
```bash
adb logcat | grep -i "vout\|video\|render"
```

2. Essayer mode audio only:
```kotlin
// Dans IPTVHomeActivity.kt
private fun playChannel(channel: IPTVChannel) {
    val uri = Uri.parse(channel.sourceUrl)
    // Tester en audio uniquement
    MediaUtils.openMediaNoUi(this, uri)
}
```

### ❌ Lecteur se Ferme

**Cause probable:** Crash ou erreur fatale

**Capturer le crash:**
```bash
adb logcat | grep -A 20 "FATAL EXCEPTION"
```

**Si OutOfMemoryError:**
- Le stream est trop lourd
- Cache trop volumineux
- Fuite mémoire

**Si NullPointerException:**
- Bug dans notre code IPTV
- Vérifier les logs "IPTV"

### ❌ Chargement Infini

**Cause:** Timeout réseau ou URL invalide

**Vérifier:**
```bash
adb logcat | grep -i "timeout\|connection\|network"
```

**Fix temporaire:** Augmenter les timeouts dans IPTVManager.kt

---

## 📊 Commandes Utiles

### Voir l'activité en cours
```bash
adb shell dumpsys activity activities | grep mResumedActivity
```

### Forcer l'arrêt de l'app
```bash
adb shell am force-stop org.videolan.vlc.debug
```

### Redémarrer l'app
```bash
adb shell am force-stop org.videolan.vlc.debug
adb shell am start -n org.videolan.vlc.debug/org.videolan.vlc.StartActivity
```

### Clear data de l'app
```bash
adb shell pm clear org.videolan.vlc.debug
```

### Vérifier les process VLC
```bash
adb shell ps | grep vlc
```

---

## 🔧 Modifications de Debug Temporaires

### 1. Ajouter Plus de Logs

Dans `IPTVHomeActivity.kt`:
```kotlin
private fun playChannel(channel: IPTVChannel) {
    try {
        Log.d(TAG, "========== DÉBUT LECTURE ==========")
        Log.d(TAG, "Chaîne: ${channel.name}")
        Log.d(TAG, "URL: ${channel.sourceUrl}")
        Log.d(TAG, "URL length: ${channel.sourceUrl.length}")
        
        val uri = Uri.parse(channel.sourceUrl)
        Log.d(TAG, "URI parsed: $uri")
        Log.d(TAG, "URI scheme: ${uri.scheme}")
        Log.d(TAG, "URI host: ${uri.host}")
        
        VideoPlayerActivity.start(this, uri, channel.name)
        
        Log.d(TAG, "VideoPlayerActivity.start() appelé")
        Log.d(TAG, "========== FIN APPEL ==========")
    } catch (e: Exception) {
        Log.e(TAG, "========== ERREUR LECTURE ==========", e)
        Log.e(TAG, "Message: ${e.message}")
        Log.e(TAG, "Cause: ${e.cause}")
        e.printStackTrace()
    }
}
```

### 2. Tester avec une URL Stable

Remplacer temporairement par une URL de test:
```kotlin
private fun playChannel(channel: IPTVChannel) {
    // Test avec une URL connue qui fonctionne
    val testUri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    VideoPlayerActivity.start(this, testUri, "Test Video")
}
```

Si ça marche → Problème avec les URLs IPTV
Si ça marche pas → Problème avec VideoPlayerActivity

---

## 📱 Tester sur Appareil Réel

**Avantages:**
- Pas de limitation d'émulateur
- Performance réelle
- Décodage matériel complet

**Commandes:**
```bash
# Vérifier appareil connecté
adb devices

# Installer
adb install -r application/app/build/outputs/apk/debug/VLC-Android-3.7.0-Beta-2-debug-all.apk

# Lancer
adb shell am start -n org.videolan.vlc.debug/org.videolan.vlc.StartActivity
```

---

## 🚨 Que Faire Maintenant?

1. **Lancez le script de debug:**
```bash
./debug_iptv.sh
```

2. **Utilisez l'app et cliquez sur une chaîne**

3. **Copiez-moi les logs** qui apparaissent

4. **Décrivez précisément:**
   - Quelle chaîne vous avez cliqué
   - Ce qui se passe exactement
   - À quel moment ça freeze

Je pourrai alors vous donner **la solution exacte** au problème!

---

**Bon debug! 🐛🔧**
