# ジェムボード管理 - GemBoard, Element, ElementBoostMatrix

## GemBoard クラス

ジェムボードの状態を管理するクラス。ジェムの移動、マッチ判定、生成を処理します。

### クラス概要

```java
public class GemBoard
```

14 個のジェムを 1 次元配列で管理。A～N のインデックスに対応。

### フィールド

| フィールド | 型          | 説明                    |
| ---------- | ----------- | ----------------------- |
| `gems`     | `Element[]` | ジェムの状態（長さ 14） |
| `RANDOM`   | `Random`    | ランダムジェム生成用    |

### コンストラクタ

#### `public GemBoard()`

ジェムボードを初期化する。

**処理内容**
14 個のジェムボードをランダムにプレイ可能な属性で初期化します。

**処理例**

```java
GemBoard board = new GemBoard();
// gems[0]～gems[13] がランダムな属性で埋まる
// 属性: FIRE, WIND, EARTH, WATER のみ
```

### メソッド

#### `public final void initialize()`

ジェムボードを再初期化する。

**処理内容**
すべてのジェムをプレイ可能な属性 (FIRE, WIND, EARTH, WATER) でランダムに初期化。

通常はコンストラクタから呼ばれます。

#### `public Element[] getGems()`

ジェムボード上の全ジェムのコピーを取得する。

**戻り値**
ジェム配列のコピー

**注意**
コピーを返すため、外部から直接ジェムボードを変更できません。

#### `public Element getGem(int index)`

指定位置のジェムを取得する。

**パラメータ**

- `index`: ジェムの位置（0～13）

**戻り値**
指定位置のジェム

**例外**

- `IndexOutOfBoundsException`: インデックスが 0～13 の範囲外

**処理例**

```java
Element gem = board.getGem(0);  // A の位置
Element gem = board.getGem(13); // N の位置
```

#### `public void swapGems(int index1, int index2)`

2 つのジェムを交換する。

**パラメータ**

- `index1`: 交換するジェムの位置 1
- `index2`: 交換するジェムの位置 2

**例外**

- `IndexOutOfBoundsException`: インデックスが範囲外

**処理例**

```java
board.swapGems(0, 1);  // A と B のジェムを交換
```

#### `public void moveGems(int fromIndex, int toIndex)`

ジェムを移動する（アニメーション表示付き）。

**パラメータ**

- `fromIndex`: 移動元のジェム位置
- `toIndex`: 移動先のジェム位置

**処理内容**
移動元から移動先まで、隣同士を順番に交換する。各交換後に画面を更新。

```
fromIndex < toIndex の場合:
  for i = fromIndex to toIndex-1:
    gems[i] <-> gems[i+1]
    Display.showGems() を呼ぶ

fromIndex > toIndex の場合:
  for i = fromIndex to toIndex+1:
    gems[i] <-> gems[i-1]
    Display.showGems() を呼ぶ
```

**処理例**

```java
board.moveGems(2, 5);
// C → D → E → F へと移動
// 画面上でアニメーション表示される
```

#### `public void moveGems(int fromIndex, int toIndex, boolean animate)`

ジェムを移動する（アニメーション表示の有無を指定）。

**パラメータ**

- `fromIndex`: 移動元のジェム位置
- `toIndex`: 移動先のジェム位置
- `animate`: アニメーション表示の有無

**処理内容**
`animate` が `false` の場合、画面更新を行わず即座に移動します。

#### `public int findMatchIndex()`

マッチしたジェムの最初の位置を探す。

**戻り値**
3 個以上連続している最初のジェムのインデックス。マッチがない場合は -1。

**マッチ条件**

```
gems[i] == gems[i+1] == gems[i+2] で、
かつそれが LIFE でも NONE でもない
```

**処理例**

```
gems = [火, 火, 火, 風, 水, ...]
findMatchIndex() → 0（火がマッチ）

gems = [火, 風, 土, 火, 火, 火, ...]
findMatchIndex() → 3（土以降の火がマッチ）
```

#### `public int getMatchCount(int index)`

指定位置から連続してマッチしているジェムの個数を取得する。

**パラメータ**

- `index`: ジェムの位置

**戻り値**
マッチしているジェムの個数（3 以上）。

**処理内容**

```
index からジェムが同じ属性で何個連続しているかカウント
```

**処理例**

```
gems = [火, 火, 火, 火, 風, ...]
getMatchCount(0) → 4

gems = [火, 火, 火, 火, 風, ...]
getMatchCount(2) → 2（正確にはマッチ判定なので本来は不適切）
```

#### `public void clearGems(int index, int count)`

指定位置から count 個のジェムを削除する。

**パラメータ**

- `index`: 削除開始位置
- `count`: 削除するジェム数

**処理内容**
指定位置から count 個のジェムを `Element.NONE` に置き換える。

**例外**

- `IndexOutOfBoundsException`: インデックスが範囲外
- `IllegalArgumentException`: count が負

**処理例**

```java
board.clearGems(0, 4);
// gems[0], gems[1], gems[2], gems[3] を削除
// gems[0:4] = [NONE, NONE, NONE, NONE, ...]
```

#### `public void shiftGems()`

空きセルを埋めるため、ジェムを移動させる（いわゆる "ジェムが落ちる" 処理）。

**処理内容**
後ろから前へスキャンして、`NONE` のセルに対して、その後ろのジェムを移動させる。

```
Right から Left へスキャン:
  NONE が見つかったら
  その NONE を持つセルまで、右側のジェムを左にシフト
```

**処理例**

```
Before: [火, NONE, 風, 火, NONE, 水, ...]
After:  [火, 風, 火, 水, ..., NONE]
```

#### `public void spawnGems()`

空きセルに新しいジェムを生成する。

**処理内容**
`NONE` であるすべてのセルにランダムなプレイ可能な属性を割り当てる。

**処理例**

```
Before: [火, 風, 火, NONE, 火, NONE, ...]
After:  [火, 風, 火, 水, 火, 土, ...]
```

#### `public boolean hasEmptyCells()`

空きセルが存在するかどうかを判定する。

**戻り値**

- `true`: `NONE` が 1 個以上存在
- `false`: `NONE` が存在しない

**処理例**

```java
if (board.hasEmptyCells()) {
    board.shiftGems();
    board.spawnGems();
}
```

---

## Element enum

ゲーム内の属性を定義する列挙型。

### クラス概要

```java
public enum Element
```

### 定義

| 属性 | 定数  | 表示名 | シンボル | 説明         |
| ---- | ----- | ------ | -------- | ------------ |
| 火   | FIRE  | "火"   | "$"      | 攻撃属性     |
| 風   | WIND  | "風"   | "@"      | 攻撃属性     |
| 土   | EARTH | "土"   | "#"      | 攻撃属性     |
| 水   | WATER | "水"   | "~"      | 攻撃属性     |
| 命   | LIFE  | "命"   | "&"      | 回復専用属性 |
| 無   | NONE  | "無"   | " "      | 空きセル     |

### メソッド

#### `public String getDisplayName()`

属性の表示名を取得する。

**戻り値**
表示名（"火", "風", "土", "水", "命", "無"）

#### `public String getSymbol()`

属性のシンボルを取得する。

**戻り値**
シンボル文字（$, @, #, ~, &, " "）

ターミナル表示で使用されます。

#### `public String getColorCode()`

属性の色コードを取得する。

**戻り値**
ANSI 色コード

#### `public boolean isPlayable()`

プレイ可能な属性かどうかを判定する。

**戻り値**

- `true`: FIRE, WIND, EARTH, WATER のいずれか
- `false`: LIFE, NONE

#### `public boolean isSpecial()`

特殊な属性（命または無）かどうかを判定する。

**戻り値**

- `true`: LIFE または NONE
- `false`: その他

#### `public static Element randomPlayable(Random random)`

プレイ可能な属性からランダムに 1 つ選択する。

**パラメータ**

- `random`: 乱数生成器

**戻り値**
ランダムに選ばれたプレイ可能な属性（FIRE, WIND, EARTH, WATER のいずれか）

**処理例**

```java
Element gem = Element.randomPlayable(new Random());
// FIRE, WIND, EARTH, WATER のいずれかがランダムに返される
```

---

## ElementBoostMatrix クラス

属性相性の行列を管理するクラス。

### クラス概要

```java
public class ElementBoostMatrix
```

属性同士の相性倍率を定義。`EnumMap` を使用して属性依存性を排除。

### 相性表

| 攻撃者 → 防御者 | 火  | 風  | 土  | 水  | 命  | 無  |
| --------------- | --- | --- | --- | --- | --- | --- |
| **火**          | 1.0 | 2.0 | 0.5 | 1.0 | 1.0 | 1.0 |
| **風**          | 0.5 | 1.0 | 2.0 | 1.0 | 1.0 | 1.0 |
| **土**          | 2.0 | 0.5 | 1.0 | 1.0 | 1.0 | 1.0 |
| **水**          | 1.0 | 1.0 | 1.0 | 1.0 | 1.0 | 1.0 |
| **命**          | 1.0 | 1.0 | 1.0 | 1.0 | 1.0 | 1.0 |
| **無**          | 1.0 | 1.0 | 1.0 | 1.0 | 1.0 | 1.0 |

### コンストラクタ

#### `public ElementBoostMatrix()`

相性行列を初期化する。

### メソッド

#### `public double getBoost(Element attacker, Element defender)`

攻撃側と防御側の相性倍率を取得。

**パラメータ**

- `attacker`: 攻撃者の属性
- `defender`: 防御者の属性

**戻り値**
相性倍率

- `2.0`: 効果抜群
- `1.0`: 等倍
- `0.5`: 今ひとつ

attacker または defender が null の場合は `1.0` を返す。

**処理例**

```java
double boost = matrix.getBoost(Element.FIRE, Element.WIND);
// 2.0（火が風に効果抜群）

double boost = matrix.getBoost(Element.FIRE, Element.EARTH);
// 0.5（火が土に今ひとつ）
```
