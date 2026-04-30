# UI 表示 - Display

## Display クラス

バトル画面の表示を管理する静的クラス。パーティ情報、バトルフィールド、ジェム状態などを表示します。

### クラス概要

```java
public class Display
```

すべてのメソッドと状態が静的で、グローバルな出力制御を行います。テスト時に出力先を変更可能です。

### フィールド

| フィールド | 型               | 説明                                                   |
| ---------- | ---------------- | ------------------------------------------------------ |
| `output`   | `OutputProvider` | 出力プロバイダ（デフォルト: `StandardOutputProvider`） |

### メソッド

#### `public static void setOutputProvider(OutputProvider provider)`

出力プロバイダを設定します。テスト時に別の出力先を使用する場合に呼ぶ。

**パラメータ**

- `provider`: 新しい出力プロバイダ

**処理例**

```java
OutputProvider mockOutput = new MockOutputProvider();
Display.setOutputProvider(mockOutput);
// 以降の Display の出力は mockOutput に送られる
```

#### `public static void resetOutputProvider()`

出力プロバイダをデフォルト（`StandardOutputProvider`）にリセットする。

**処理例**

```java
Display.resetOutputProvider();
// 出力先を標準出力に戻す
```

#### `public static void showPartyInfo(Party party)`

パーティの情報を表示する。

**パラメータ**

- `party`: 表示するパーティ

**表示内容**

- "＜パーティ編成＞" というヘッダー
- 区切り線
- 各モンスターのについて：
    - モンスター名（色付き）
    - HP（3 桁）
    - 攻撃力（2 桁）
    - 防御力（2 桁）
- 区切り線

**表示例**

```
＜パーティ編成＞
-----------------------------
$朱雀$ HP=150 攻撃=30 防御=10
@青龍@ HP=150 攻撃=20 防御=10
#白虎# HP=150 攻撃=25 防御= 5
~玄武~ HP=150 攻撃=25 防御=15
-----------------------------
```

#### `public static void showBattleField(Party party, Monster enemy)`

バトルフィールドを表示する。

**パラメータ**

- `party`: プレイヤーのパーティ
- `enemy`: 敵モンスター

**表示内容**

1. "バトルフィールド" というタイトル
2. 敵モンスター名と HP（"HP = 300 / 400" のような形式）
3. 味方のモンスター名一覧（スペース区切り）
4. 味方のパーティ総 HP（"HP = 600 / 600" のような形式）
5. 区切り線
6. ジェムボード位置表示（A～N）
7. ジェムボード表示
8. 区切り線

**表示例**

```
バトルフィールド
$スライム$ HP = 100 / 100

$朱雀$ @青龍@ #白虎# ~玄武~
HP = 600 / 600
-----------------------------
A B C D E F G H I J K L M N
$ @ # ~ @ # ~ @
-----------------------------
```

#### `public static void showGems(GemBoard board)`

ジェムボードを表示する。

**パラメータ**

- `board`: 表示するジェムボード

**表示内容**
14 個のジェムをスペース区切りで 1 行に表示。各ジェムは色付きシンボル。

**表示例**

```
$ @ # ~ @ # ~ @ $ @ # ~ @ #
```

#### `public static void showMonsterName(Monster monster)`

モンスター名を色付きで表示する。

**パラメータ**

- `monster`: 表示するモンスター

**表示内容**
属性に応じた背景色でモンスター名を表示。シンボルで囲む。

形式: `シンボル + 名前 + シンボル` （ANSI 色コード付き）

**表示例（概念）**

```
[背景色は属性に応じて]
$朱雀$  （火属性なら背景色 1）
@青龍@  （風属性なら背景色 2）
```

**内部処理**

```
ANSI シーケンス: \u001B[4{colorCode}m\u001B[30m{symbol}{name}{symbol}\u001B[0m
```

#### `public static void printLine()`

区切り線を表示する。

**表示内容**
`GameData.LINE_LENGTH` 個の "-" 文字（デフォルト: 29 個）

**表示例**

```
-----------------------------
```

### 使用例

#### ゲーム開始時

```java
Party party = new Party("勇者", Arrays.asList(GameData.createPlayers()));
Display.showPartyInfo(party);
// パーティ編成を表示
```

#### バトル中

```java
BattleManager battleManager = new BattleManager(party, enemy, commandReader);
// battle() メソッド内で複数回呼ばれる
Display.showBattleField(party, enemy);
Display.showGems(party.getGemBoard());
Display.showMonsterName(friend);
Display.printLine();
```

#### テスト時

```java
// テスト用の出力キャプチャ
List<String> outputs = new ArrayList<>();
OutputProvider testOutput = new OutputProvider() {
    @Override
    public void println(String text) {
        outputs.add(text);
    }
    // 他のメソッドも実装...
};

Display.setOutputProvider(testOutput);
Display.showPartyInfo(party);

// outputs に出力がキャプチャされている
assert outputs.contains("＜パーティ編成＞");
```

### 表示の特徴

- **色付き表示**: ANSI 色コードを使用
    - シンボルも含めて背景色で表示
    - ターミナルで対応していない場合は色コードが見えるが、機能は変わらない

- **静的メソッド**: グローバルな出力制御を実現
    - ゲーム全体で統一された出力先

- **出力プロバイダの抽象化**: テスト・ロギング・GUI 化に対応可能
    - `OutputProvider` インターフェースにより実装を切り替え可能
