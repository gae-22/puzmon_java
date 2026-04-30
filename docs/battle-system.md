# バトルシステム - BattleManager, DamageCalculator

## BattleManager クラス

バトルの全体進行を管理するクラス。プレイヤーターンと敵ターンを交互に実行し、ジェム評価やダメージ計算を統括します。

### クラス概要

```java
public class BattleManager
```

### コンストラクタ

#### `public BattleManager(Party party, Monster enemy, CommandReader commandReader)`

**パラメータ**

- `party`: プレイヤーのパーティ
- `enemy`: 敵モンスター
- `commandReader`: コマンド入力用リーダー

デフォルトの `StandardOutputProvider` を使用します。

#### `public BattleManager(Party party, Monster enemy, CommandReader commandReader, OutputProvider output)`

**パラメータ**

- `party`: プレイヤーのパーティ
- `enemy`: 敵モンスター
- `commandReader`: コマンド入力用リーダー
- `output`: 出力プロバイダ

### メソッド

#### `public boolean battle()`

バトルを開始する。プレイヤーターンと敵ターンを交互に繰り返し、どちらかが戦闘不能になるまで続けます。

**戻り値**

- `true`: プレイヤーが勝利（敵を全滅）
- `false`: プレイヤーが敗北（パーティ全滅）

**処理フロー**

```
敵出現表示
↓
ループ {
  1. プレイヤーターン実行
  2. 敵が戦闘不能？ → true なら勝利で終了
  3. 敵ターン実行
  4. パーティが戦闘不能？ → true なら敗北で終了
}
```

**処理例**

```java
BattleManager manager = new BattleManager(party, enemy, commandReader);
boolean won = manager.battle();
if (won) {
    // 敵を倒した
} else {
    // パーティが全滅した
}
```

#### `public void playerTurn()`

プレイヤーターンを実行する。

**処理内容**

1. ターン情報表示（パーティ名、HP）
2. バトルフィールド表示
3. コマンド入力（ジェム交換位置の入力）
4. ジェム移動実行
5. ジェム評価処理（`evaluateGems()`）

**ジェム入力**
プレイヤーは A～N の範囲から 2 文字を入力。例："AC" は A から C への移動。

#### `public void enemyTurn()`

敵ターンを実行する。敵がプレイヤーパーティにダメージを与えます。

**ダメージ計算**

```
基本ダメージ = 敵攻撃力 - パーティ平均防御力
最終ダメージ = max(1, 基本ダメージ × ランダム因子)
```

ランダム因子は -10% ～ +10% の範囲。

**処理内容**

1. 敵ターン情報表示
2. ダメージ計算
3. パーティにダメージを与える
4. ダメージ量を表示

#### `private void evaluateGems()`

ジェムを評価し、マッチしたジェムに対してアクションを実行する。コンボカウントを管理。

**処理フロー**

```
ループ {
  1. マッチするジェムを探す（findMatchIndex）
  2. マッチなし？ → ループ終了
  3. マッチしたジェム数を取得
  4. ジェムの属性を取得
  5. ジェムを削除（clearGems）

  属性に応じた処理：
  - LIFE属性: 回復処理（doRecover）
  - その他: 対応するモンスターが攻撃（doAttack）

  6. 空きセルがある？
     - ある場合: ジェムを落下させ（shiftGems）、新規生成（spawnGems）
}
```

**コンボ管理**
マッチが発生するたびに `combo` をインクリメント。コンボボーナスはダメージとヒーリング計算に反映される。

#### `private void doAttack(Monster friend, int matchCount, int combo)`

攻撃を実行する。

**パラメータ**

- `friend`: 攻撃側のモンスター
- `matchCount`: マッチしたジェム数
- `combo`: コンボカウント

**ダメージ計算**
`DamageCalculator` の `calculateFinalDamage()` を使用。

- 基本ダメージ
- 属性相性倍率
- コンボ倍率
- ランダム因子

**処理内容**

1. 最終ダメージを計算
2. モンスター名と "の攻撃！" を表示
3. コンボ数が 2 以上なら " N Combo!!" と表示
4. 敵に対するダメージを表示
5. 敵にダメージを与える

**処理例**

```
青龍の攻撃! 3 Combo!!
スライムに 45 のダメージを与えた
```

#### `private void doRecover(int matchCount, int combo)`

パーティを回復する。

**パラメータ**

- `matchCount`: マッチした LIFE ジェム数
- `combo`: コンボカウント

**回復量計算**

```
コンボ倍率 = getComboBoost(matchCount, combo)
回復量 = max(1, 20 × コンボ倍率 × ランダム因子)
```

**処理内容**

1. コンボ倍率を計算
2. 回復量を計算
3. 実際の回復量を計算（最大 HP を超えないようにクリップ）
4. 回復メッセージを表示

---

## DamageCalculator クラス

ダメージ計算エンジン。基本ダメージ、属性相性、コンボ倍率、ランダム因子を統合して最終ダメージを計算します。

### クラス概要

```java
public class DamageCalculator
```

### メソッド

#### `public int getBaseDamage(int attackPower, int defensePower)`

基本ダメージを計算する。

**計算式**

```
基本ダメージ = max(0, 攻撃力 - 防御力)
```

**パラメータ**

- `attackPower`: 攻撃力
- `defensePower`: 防御力

**戻り値**
基本ダメージ（非負整数）

**例外**

- `IllegalArgumentException`: 攻撃力または防御力が負の場合

**処理例**

```java
int damage = calculator.getBaseDamage(30, 10);
// damage == 20
```

#### `public double getElementBoost(Element attacker, Element defender)`

属性相性倍率を取得する。

**パラメータ**

- `attacker`: 攻撃者の属性
- `defender`: 防御者の属性

**戻り値**
相性倍率

- `2.0`: 効果抜群
- `1.0`: 等倍
- `0.5`: 今ひとつ

**相性関係**

- 火 → 風に 2.0 倍、土に 0.5 倍
- 風 → 土に 2.0 倍、火に 0.5 倍
- 土 → 水に 2.0 倍、風に 0.5 倍
- 水 → すべてに等倍
- 命・無 → すべてに等倍

**処理例**

```java
double boost = calculator.getElementBoost(Element.FIRE, Element.WIND);
// boost == 2.0（火が風に効果抜群）
```

#### `public double getComboBoost(int gemCount, int comboCount)`

コンボ倍率を計算する。

**計算式**

```
ブースト値 = max(1, ジェム数 - 3 + コンボ回数)
コンボ倍率 = 1.5 ^ ブースト値
```

**パラメータ**

- `gemCount`: マッチしたジェム数（最小 3）
- `comboCount`: コンボ回数（0 以上）

**戻り値**
コンボ倍率（1.0 以上）

**例外**

- `IllegalArgumentException`: ジェム数またはコンボ数が負の場合

**計算例**

```
ジェム数=3, コンボ=0: 1.5^1 = 1.5
ジェム数=4, コンボ=0: 1.5^2 = 2.25
ジェム数=3, コンボ=2: 1.5^3 = 3.375
```

**処理例**

```java
double boost = calculator.getComboBoost(3, 1);
// boost == 2.25（1.5^2）
```

#### `public double getRandomFactor()`

ランダム因子を取得する。

**戻り値**
ランダムな倍率（0.9 ～ 1.1）

-10% ～ +10% の範囲でランダムな値を返す。毎回異なる値。

**処理例**

```java
double random = calculator.getRandomFactor();
// 0.95, 1.03, 0.88 などがランダムに返される
```

#### `public int calculateFinalDamage(int attackPower, int defensePower, Element attackerElement, Element defenderElement, int gemCount, int comboCount)`

最終ダメージを計算する。

**計算式**

```
基本ダメージ = getBaseDamage(攻撃力, 防御力)
属性相性倍率 = getElementBoost(攻撃者属性, 防御者属性)
コンボ倍率 = getComboBoost(ジェム数, コンボ数)
ランダム因子 = getRandomFactor()

最終ダメージ = max(1, 基本ダメージ × 属性相性 × コンボ倍率 × ランダム因子)
```

**パラメータ**

- `attackPower`: 攻撃力
- `defensePower`: 防御力
- `attackerElement`: 攻撃者の属性
- `defenderElement`: 防御者の属性
- `gemCount`: マッチしたジェム数
- `comboCount`: コンボ回数

**戻り値**
最終ダメージ（最小値 1）

**処理例**

```java
int damage = calculator.calculateFinalDamage(
    30,           // 攻撃力
    10,           // 防御力
    Element.FIRE, // 攻撃者（火）
    Element.WIND, // 防御者（風）
    3,            // ジェム数
    1             // コンボ数
);
// 計算: 20 × 2.0 × 2.25 × (ランダム 0.9～1.1)
// 結果: 90 付近の値
```
