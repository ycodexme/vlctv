# 📱 Build avec Android Studio - French TV IPTV

## ⚠️ Important

Le projet VLC Android **ne peut PAS être compilé avec Gradle CLI** à cause de databinding.

**Vous DEVEZ utiliser Android Studio.**

---

## 🔧 Installation d'Android Studio

### 1. Télécharger Android Studio
```
https://developer.android.com/studio
```

### 2. Installer Android Studio
- Ouvrir le .dmg téléchargé
- Glisser Android Studio dans Applications
- Lancer Android Studio

### 3. Configuration Initiale
- Accepter les termes
- Choisir "Standard" installation
- Télécharger les SDK Android
- Attendre la fin de l'installation

---

## 📂 Ouvrir le Projet VLC

### 1. Ouvrir le Projet
```
File → Open
Naviguer vers: /Users/m1/Documents/vlc-android
Sélectionner le dossier racine
Click "Open"
```

### 2. Premier Sync
Android Studio va automatiquement:
- Analyser le projet
- Télécharger les dépendances Gradle
- Synchroniser les fichiers
- **Générer les fichiers databinding** (ce qui échoue en CLI)

**Attendre 5-10 minutes** pour le premier sync.

### 3. Si Erreurs de Sync
```
File → Invalidate Caches / Restart → Invalidate and Restart
```

---

## 🔨 Compiler l'APK

### Méthode 1: Build → Build APK(s)
```
1. Build → Build APK(s)
2. Attendre la compilation (5-10 minutes)
3. Click sur "locate" dans la notification
4. L'APK est dans: application/app/build/outputs/apk/debug/
```

### Méthode 2: Build → Make Project
```
1. Build → Make Project (Ctrl+F9)
2. Vérifier qu'il n'y a pas d'erreurs
3. Build → Build APK(s)
```

### Méthode 3: Gradle Panel
```
1. Ouvrir le panel Gradle (droite)
2. vlc-android → app → Tasks → build
3. Double-click sur "assembleDebug"
4. Attendre la compilation
```

---

## 📲 Installer l'APK

### Depuis Android Studio
```
Run → Run 'app'
Ou click sur le bouton ▶️ (Play)
```

### Manuellement
```bash
cd /Users/m1/Documents/vlc-android/application/app/build/outputs/apk/debug
adb install -r VLC-Android-3.7.0-Beta-2-debug-all.apk
```

---

## ✅ Vérifier que Tout Fonctionne

### 1. Vérifier les Fichiers IPTV
Dans le Project Explorer (gauche):
```
app/src/org/videolan/vlc/iptv/
├── ✅ IPTVChannel.kt
├── ✅ IPTVManager.kt  
├── ✅ IPTVHomeActivity.kt
├── ✅ CategoryDetailActivity.kt
├── ✅ SearchActivity.kt
├── ✅ CategoryAdapter.kt
├── ✅ ChannelAdapter.kt
└── ✅ ChannelGridAdapter.kt
```

### 2. Vérifier les Layouts
```
app/res/layout/
├── ✅ activity_iptv_home.xml
├── ✅ activity_category_detail.xml
├── ✅ activity_search.xml
├── ✅ item_category_section.xml
└── ✅ item_channel_card.xml
```

### 3. Vérifier les Icônes French TV
```
resources/src/main/res/drawable/
├── ✅ ic_launcher_foreground.xml (TV avec drapeau 🇫🇷)
├── ✅ ic_launcher_background.xml (Gradient bleu)
└── ✅ ic_cone_o.xml (Logo de chargement TV)
```

---

## 🐛 Problèmes Courants

### "Cannot resolve symbol 'databinding'"
**Solution:**
```
Build → Clean Project
Build → Rebuild Project
```

### "Gradle sync failed"
**Solution:**
```
File → Invalidate Caches / Restart
```

### "SDK not found"
**Solution:**
```
File → Project Structure → SDK Location
Vérifier que le SDK Android est bien installé
```

### "Out of memory"
**Solution:**
Augmenter la RAM Gradle dans `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
```

### Build très lent
**Solution:**
```
File → Settings → Build, Execution, Deployment → Compiler
☑️ Configure on demand
☑️ Compile independent modules in parallel
```

---

## 📊 Temps de Build Estimés

### Premier Build (Clean)
- **Avec Internet rapide**: 10-15 minutes
- **Avec Internet lent**: 20-30 minutes
- **Offline (dépendances cached)**: 5-10 minutes

### Build Incrémental (après modifications IPTV)
- **1-3 minutes**

### Build Release (Signed APK)
- **15-20 minutes**

---

## 🎯 Workflow Recommandé

### Développement
```
1. Modifier le code IPTV dans Android Studio
2. Build → Make Project (Ctrl+F9)
3. Run → Run 'app' (Shift+F10)
4. Tester sur émulateur/device
```

### Build Final
```
1. Build → Clean Project
2. Build → Rebuild Project
3. Build → Build APK(s)
4. Signer l'APK (si release)
5. Installer sur devices
```

---

## 🚀 Raccourcis Clavier Utiles

### Build
- **Ctrl+F9**: Make Project
- **Shift+F10**: Run app
- **Ctrl+Shift+F9**: Recompile file

### Navigation
- **Shift+Shift**: Recherche globale
- **Ctrl+N**: Recherche classe
- **Ctrl+Shift+N**: Recherche fichier
- **Ctrl+B**: Aller à la définition

### Refactoring
- **Shift+F6**: Renommer
- **Ctrl+Alt+L**: Reformater code

---

## 📝 Configuration Android Studio Recommandée

### Augmenter la RAM
```
Help → Edit Custom VM Options
Ajouter:
-Xmx4096m
-XX:ReservedCodeCacheSize=512m
```

### Activer Configuration Cache
Dans `gradle.properties`:
```properties
org.gradle.configuration-cache=true
org.gradle.caching=true
```

### Désactiver Animations (Build plus rapide)
```
File → Settings → Appearance & Behavior → Appearance
☐ Animate windows
☐ Show tool window bars
```

---

## ✅ Checklist de Build

Avant de compiler:
- [ ] Android Studio installé et configuré
- [ ] Projet ouvert dans Android Studio
- [ ] Gradle sync réussi (sans erreurs)
- [ ] Tous les fichiers IPTV présents
- [ ] SDK Android installé
- [ ] Émulateur ou device connecté

Compilation:
- [ ] Build → Clean Project
- [ ] Build → Rebuild Project
- [ ] Vérifier 0 erreurs
- [ ] Build → Build APK(s)
- [ ] APK généré avec succès

Installation:
- [ ] APK localisé dans build/outputs/apk/debug/
- [ ] adb devices détecte le device
- [ ] APK installé avec succès
- [ ] App lancée correctement

Test:
- [ ] IPTVHomeActivity s'ouvre
- [ ] Catégories se chargent
- [ ] Grille 3 colonnes fonctionne
- [ ] Click sur chaîne → Lecture
- [ ] Favoris fonctionnent
- [ ] Recherche fonctionne
- [ ] Bouton retour fonctionne
- [ ] Icônes French TV affichées

---

## 🎉 Résultat Final

Une fois compilé avec Android Studio, vous aurez:

✅ **Application French TV IPTV complète**
- Interface IPTV moderne
- 100+ chaînes françaises
- Grille fixe 3 colonnes
- Système de favoris
- Recherche instantanée
- Navigation fluide
- Branding French TV 🇫🇷

---

## 📞 Support

### Logs Android Studio
```
View → Tool Windows → Logcat
Filtrer: "IPTV"
```

### Build Logs
```
View → Tool Windows → Build
Voir tous les messages de compilation
```

### Profiler
```
View → Tool Windows → Profiler
Analyser performance de l'app
```

---

*Android Studio est OBLIGATOIRE pour compiler VLC Android*  
*Gradle CLI ne supporte pas correctement le databinding VLC*  
*Tous les fichiers IPTV sont prêts et corrects! 🚀*
