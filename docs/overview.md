# Puzzle & Monsters - プロジェクト概要

## プロジェクト構成

このドキュメントは、Puzzle & Monsters ゲームの各クラス・メソッドの詳細仕様を記述しています。

## パッケージ構成

```
puzmon/
├── input/              # 入力抽象層
│   ├── InputProvider.java              (I)
│   └── SystemInInputProvider.java      (C)
├── output/             # 出力抽象層
│   ├── OutputProvider.java             (I)
│   └── StandardOutputProvider.java     (C)
├── BattleManager.java  (C)    # バトル管理
├── CommandReader.java  (C)    # コマンド入力処理
├── DamageCalculator.java (C)  # ダメージ計算
├── Display.java        (C)    # UI表示制御
├── Element.java        (E)    # 属性列挙型
├── ElementBoostMatrix.java (C) # 相性行列
├── GameData.java       (C)    # ゲーム定数・データ生成
├── GemBoard.java       (C)    # ジェムボード状態管理
├── Main.java           (C)    # ゲーム進行管理
├── Monster.java        (C)    # モンスター
└── Party.java          (C)    # パーティ（複数モンスター）

(I)=Interface, (C)=Class, (E)=Enum
```

## アーキテクチャ概要

### 層構造

**表示層（UI）**

- `Display`: 画面表示制御
- `OutputProvider`, `StandardOutputProvider`: 出力抽象化

**入力層**

- `CommandReader`: ユーザーコマンド解析
- `InputProvider`, `SystemInInputProvider`: 入力抽象化

**ゲームロジック層**

- `Main`: ゲーム進行管理
- `BattleManager`: バトル進行管理
- `DamageCalculator`: ダメージ計算エンジン

**データ層**

- `Monster`: モンスターデータ
- `Party`: パーティ管理
- `GemBoard`: ジェムボード状態
- `Element`: 属性定義
- `ElementBoostMatrix`: 相性定義
- `GameData`: ゲーム定数・初期データ

### 依存関係

```
Main
 ├── Party
 │    ├── Monster
 │    └── GemBoard
 │         ├── Element
 │         └── Display
 ├── BattleManager
 │    ├── Party
 │    ├── Monster
 │    ├── CommandReader
 │    ├── DamageCalculator
 │    │    ├── Element
 │    │    └── ElementBoostMatrix
 │    ├── Display
 │    └── OutputProvider
 ├── CommandReader
 │    ├── InputProvider
 │    └── OutputProvider
 └── GameData
```

## 仕様書の構成

- `game-flow.md` - ゲーム管理とゲームループ
- `battle-system.md` - バトルシステムとダメージ計算
- `character.md` - キャラクター（モンスター・パーティ）管理
- `gem-board.md` - ジェムボードと属性相性
- `ui-display.md` - UI表示制御
- `io-system.md` - 入出力システム
