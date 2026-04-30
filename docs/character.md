# キャラクター管理 - Monster, Party

## Monster クラス

モンスターデータを保持するクラス。名前、体力、属性、攻撃力、防御力の情報を管理します。

### クラス概要

```java
public class Monster
```

### フィールド

| フィールド     | 型        | 説明         |
| -------------- | --------- | ------------ |
| `name`         | `String`  | モンスター名 |
| `maxHp`        | `int`     | 最大 HP      |
| `element`      | `Element` | 属性         |
| `attackPower`  | `int`     | 攻撃力       |
| `defensePower` | `int`     | 防御力       |
| `hp`           | `int`     | 現在の HP    |

### コンストラクタ

#### `public Monster(String name, int maxHp, Element element, int attackPower, int defensePower)`

モンスターを生成する。

**パラメータ**

- `name`: モンスター名
- `maxHp`: 最大 HP（これが初期 HP として設定される）
- `element`: 属性
- `attackPower`: 攻撃力
- `defensePower`: 防御力

**処理例**

```java
Monster slime = new Monster("スライム", 100, Element.WATER, 10, 1);
// HP は自動的に 100 に設定される
```

### メソッド

#### `public String getName()`

モンスター名を取得する。

**戻り値**
モンスター名

#### `public int getHp()`

現在の HP を取得する。

**戻り値**
現在の HP（0 以上、最大 HP 以下）

#### `public int getMaxHp()`

最大 HP を取得する。

**戻り値**
最大 HP

#### `public Element getElement()`

属性を取得する。

**戻り値**
属性（FIRE, WIND, EARTH, WATER, LIFE, NONE）

#### `public int getAttackPower()`

攻撃力を取得する。

**戻り値**
攻撃力

#### `public int getDefensePower()`

防御力を取得する。

**戻り値**
防御力

#### `public void takeDamage(int damage)`

ダメージを受ける。

**パラメータ**

- `damage`: 受けるダメージ

**処理内容**

```
現在HP = max(0, 現在HP - max(0, ダメージ))
```

HP が 0 未満にならないようにクリップされます。負のダメージは受け付けません。

**処理例**

```java
monster.takeDamage(25);
// HP が 25 だけ減少（0 未満にはならない）

monster.takeDamage(-5);
// 何もしない（負のダメージは無視）
```

#### `public void heal(int amount)`

HP を回復する。

**パラメータ**

- `amount`: 回復量

**処理内容**

```
現在HP = min(最大HP, 現在HP + max(0, 回復量))
```

HP が最大 HP を超えないようにクリップされます。負の回復は受け付けません。

**処理例**

```java
monster.heal(30);
// HP が 30 回復（最大 HP を超えない）

monster.heal(-10);
// 何もしない（負の回復は無視）
```

#### `public boolean isDead()`

戦闘不能状態かどうかを判定する。

**戻り値**

- `true`: HP が 0 以下
- `false`: HP が 1 以上

---

## Party クラス

複数のモンスター、ジェムボード、総 HP、平均防御力を管理するパーティクラス。

### クラス概要

```java
public class Party
```

### フィールド

| フィールド       | 型              | 説明                         |
| ---------------- | --------------- | ---------------------------- |
| `name`           | `String`        | パーティ名                   |
| `friends`        | `List<Monster>` | パーティに属するモンスター   |
| `gemBoard`       | `GemBoard`      | ジェムボード                 |
| `hp`             | `int`           | 総 HP                        |
| `maxHp`          | `int`           | 最大総 HP                    |
| `averageDefense` | `int`           | 平均防御力（小数点切り上げ） |

### コンストラクタ

#### `public Party(String name, List<Monster> friends)`

パーティを生成する。

**パラメータ**

- `name`: パーティ名
- `friends`: パーティに属するモンスターのリスト

**例外**

- `IllegalArgumentException`: friends が null または空の場合

**初期化処理**

1. 名前を設定
2. モンスターリストをコピー（外部から変更されないようにする）
3. ジェムボードを生成
4. 総 HP を計算（全モンスターの HP の合計）
5. 最大総 HP を計算（全モンスターの最大 HP の合計）
6. 平均防御力を計算（合計防御力 ÷ モンスター数、切り上げ）

**処理例**

```java
List<Monster> monsters = Arrays.asList(
    new Monster("朱雀", 150, Element.FIRE, 30, 10),
    new Monster("青龍", 150, Element.WIND, 20, 10)
);
Party party = new Party("勇者パーティ", monsters);
// totalHp = 300, maxHp = 300, averageDefense = 10
```

#### `public Party(String name, Monster[] friends)`

パーティを生成する（配列版）。

**パラメータ**

- `name`: パーティ名
- `friends`: パーティに属するモンスターの配列

内部的には `List` 版コンストラクタに委譲します。

### メソッド

#### `public String getName()`

パーティ名を取得する。

**戻り値**
パーティ名

#### `public List<Monster> getFriends()`

パーティに属するモンスターのリストを取得する。

**戻り値**
モンスターのリスト（不変ビュー）

外部からの変更ができないようにラップされています。

#### `public GemBoard getGemBoard()`

ジェムボードを取得する。

**戻り値**
ジェムボード

#### `public int getTotalHp()`

パーティの総 HP を取得する。

**戻り値**
総 HP（0 以上、最大総 HP 以下）

#### `public int getMaxHp()`

パーティの最大総 HP を取得する。

**戻り値**
最大総 HP

#### `public int getAverageDefense()`

パーティの平均防御力を取得する。

**計算式**

```
平均防御力 = ceil(合計防御力 / モンスター数)
```

**戻り値**
平均防御力（整数、小数点切り上げ）

**例**

```
モンスター1: 防御力=10
モンスター2: 防御力=11
モンスター3: 防御力=12
平均 = ceil(33/3) = ceil(11) = 11
```

#### `public void takeDamage(int damage)`

パーティがダメージを受ける。

**パラメータ**

- `damage`: 受けるダメージ

**処理内容**

```
総HP = max(0, 総HP - max(0, ダメージ))
```

HP が 0 未満にならないようにクリップされます。

#### `public void heal(int amount)`

パーティを回復する。

**パラメータ**

- `amount`: 回復量

**処理内容**

```
総HP = min(最大HP, 総HP + max(0, 回復量))
```

HP が最大 HP を超えないようにクリップされます。

#### `public boolean isDefeated()`

パーティが戦闘不能状態かどうかを判定する。

**戻り値**

- `true`: HP が 0 以下
- `false`: HP が 1 以上

#### `public Monster findFriendByElement(Element element)`

指定された属性のモンスターをパーティから探す。

**パラメータ**

- `element`: 検索する属性

**戻り値**
該当するモンスター。見つからない場合は `null`。

複数該当する場合は最初に見つかったものを返す。

**処理例**

```java
Monster friend = party.findFriendByElement(Element.FIRE);
if (friend != null) {
    // 火属性のモンスターを使って攻撃
}
```

---

## 相互作用例

### パーティ初期化時の HP 計算

```
Party party = new Party("勇者", [朱雀(150HP), 青龍(150HP), 白虎(150HP), 玄武(150HP)])

total = 150 + 150 + 150 + 150 = 600
maxHp = 600
averageDefense = ceil((10+10+5+15)/4) = ceil(40/4) = 10
```

### バトル中のダメージと回復

```
敵ターン: パーティが 50 ダメージ受ける
  party.takeDamage(50)
  総HP: 600 → 550

プレイヤーターン: LIFE ジェム 4 個マッチ、コンボ 1 回で回復
  party.heal(calculatedAmount)
  総HP: 550 → 590 (等)
```
