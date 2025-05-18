# 🏹 Système de Tir de Flèche avec Gravité – Terraria-like (Java)

Ce module implémente un système de tir de flèche dans un jeu 2D (type Terraria) en Java, en utilisant des mathématiques simples de vecteurs et de physique. Les flèches suivent une **trajectoire réaliste (parabole)** influencée par la **gravité**.

---

## 🔧 Fonctionnement du tir

### Étape 1 : Vecteur direction

Soit :

- `P = (xp, yp)` : position du joueur  
- `M = (xm, ym)` : position de la souris (curseur de visée)

Le **vecteur direction** est :

d⃗ = M − P = (xm − xp, ym − yp)
---

### Étape 2 : Normalisation du vecteur

Pour que la flèche ait une **vitesse constante**, on normalise le vecteur direction :

‖d⃗‖ = √((xm − xp)² + (ym − yp)²)

d⃗_normalisé = (
(xm − xp) / ‖d⃗‖,
(ym − yp) / ‖d⃗‖
)


---

### Étape 3 : Appliquer une vitesse

Soit `v` la vitesse voulue (ex: `v = 10`).  
La **vitesse finale** de la flèche est :

v⃗ = v × d⃗_normalisé


Chaque frame, la flèche avance de :

x += v⃗.x
y += v⃗.y
---

## 🧮 Gravité

La **gravité** est un ajout à la vitesse verticale de la flèche.

À chaque frame :

v_y += g // gravité (ex: g = 0.3)
y += v_y // on applique la vitesse verticale
Cela crée une **trajectoire en arc**, comme un vrai projectile.

---

## 📐 Exemple (valeurs réelles)

- Position du joueur : `(100, 100)`
- Position souris : `(200, 300)`
- Vitesse `v = 10`

### Calculs :

d⃗ = (100, 200)
‖d⃗‖ ≈ 223.6
d⃗_normalisé ≈ (0.447, 0.894)
v⃗ ≈ (4.47, 8.94)
La flèche avance d’environ `+4.47 px` en X et `+8.94 px` en Y à chaque frame (plus avec la gravité).

---

## 🚀 Extensions possibles

- ✅ Ajout de collisions (AABB ou cercles)
- 🎯 Ajout d'effets de particules ou rotation selon la trajectoire
- 🛑 Détection du sol ou d'obstacles
- 🧠 IA pour viser automatiquement (auto-targeting)

---

## 📁 Structure recommandée

src/

├── GamePanel.java // Boucle de jeu, clavier, dessin

├── Player.java // Classe du joueur

├── Arrow.java // Classe des flèches

├── utils/

│ └── Vector2D.java // (optionnel) gestion propre des vecteurs
---

## 📜 Licence

Projet libre d’utilisation à but pédagogique.

---

