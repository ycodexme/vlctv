from PIL import Image
import os

# Définition des dimensions des mipmaps
mipmap_sizes = {
    "mipmap-mdpi": (48, 48),   # Downscaled from 128x128
    "mipmap-hdpi": (72, 72),   # Downscaled from 192x192
    "mipmap-xhdpi": (96, 96),  # Downscaled from 256x256
    "mipmap-xxhdpi": (144, 144),  # Downscaled from 384x384
    "mipmap-xxxhdpi": (192, 192),  # Downscaled from 512x512
}

# Définition des dimensions pour les icônes de notification
notification_sizes = {
    "drawable": (24, 24),  # Taille de base pour les notifications
    "drawable-hdpi": (36, 36),
    "drawable-xhdpi": (48, 48),
    "drawable-xxhdpi": (72, 72),
    "drawable-xxxhdpi": (96, 96),
}

# Image source (doit être 512x512)
source_image_path = "assets/frenchtv.png"  # Remplacez par le chemin de votre image source

# Dossier de sortie
output_dir = "android/app/src/main/res"

# Vérifie si l'image source existe
if not os.path.exists(source_image_path):
    print(f"Erreur : L'image source '{source_image_path}' est introuvable.")
    exit(1)

# Charge l'image source
original_image = Image.open(source_image_path)

# Création des dossiers et redimensionnement pour les mipmaps (icône de l'app)
print("\n🎯 Génération des icônes de l'application...")
for folder, size in mipmap_sizes.items():
    folder_path = os.path.join(output_dir, folder)
    os.makedirs(folder_path, exist_ok=True)  # Crée le dossier s'il n'existe pas
    
    resized_image = original_image.resize(size, Image.LANCZOS)
    output_path = os.path.join(folder_path, "ic_launcher.png")
    
    resized_image.save(output_path, format="PNG")
    print(f"✅ Image launcher enregistrée : {output_path}")

# Création des dossiers et redimensionnement pour les notifications
print("\n🔔 Génération des icônes de notification...")
for folder, size in notification_sizes.items():
    folder_path = os.path.join(output_dir, folder)
    os.makedirs(folder_path, exist_ok=True)
    
    resized_image = original_image.resize(size, Image.LANCZOS)
    output_path = os.path.join(folder_path, "notification_icon.png")
    
    resized_image.save(output_path, format="PNG")
    print(f"✅ Image notification enregistrée : {output_path}")

print("\n📦 Toutes les icônes ont été générées avec succès !")
