# 入出力システム - CommandReader, InputProvider, OutputProvider

## CommandReader クラス

プレイヤーのコマンド入力を管理するクラス。プレイヤー名の入力、ジェム移動コマンドの入力と検証を行います。

### クラス概要

```java
public class CommandReader
```

### コンストラクタ

#### `public CommandReader()`

デフォルトの入出力プロバイダを使用。

#### `public CommandReader(InputProvider inputProvider)`

入力プロバイダを指定。出力は `StandardOutputProvider` を使用。

**パラメータ**

- `inputProvider`: 入力プロバイダ

#### `public CommandReader(InputProvider inputProvider, OutputProvider output)`

入出力プロバイダを指定。

**パラメータ**

- `inputProvider`: 入力プロバイダ
- `output`: 出力プロバイダ

### メソッド

#### `public String readPlayerName()`

プレイヤー名を入力させる。

**処理フロー**

```
ループ {
  "プレイヤー名を入力してください>" を表示
  入力を取得
  入力が空文字列？
    → true: "エラー：プレイヤー名を入力してください" と表示して再ループ
    → false: 入力されたプレイヤー名を返す
}
```

**戻り値**
入力されたプレイヤー名（空文字列以外）

**ユーザーインタラクション**

```
プレイヤー名を入力してください>（ユーザーが何も入力しない）
エラー：プレイヤー名を入力してください
プレイヤー名を入力してください>太郎
→ 戻り値: "太郎"
```

#### `public String readCommand()`

ジェム移動コマンドを入力させる。

**処理フロー**

```
ループ {
  "コマンド？>" を表示
  入力を取得
  入力が有効？（isValidCommand チェック）
    → true: 入力を返す
    → false: エラーメッセージを表示して再ループ
}
```

**戻り値**
有効な 2 文字コマンド（例: "AC", "1N"）

**コマンド形式**

```
2文字の英数字
範囲: A～N または 0～9
例: AC, 1A, N0, 05
```

**ユーザーインタラクション**

```
コマンド？>A（1文字しか入力しない）
2文字で入力して下さい。
コマンド？>AA（同じ文字）
1文字目と2文字目が同じ値です
コマンド？>AC（有効な入力）
→ 戻り値: "AC"
```

#### `public int[] parseCommand(String command)`

コマンド文字列をパースして移動前後のジェムインデックスを取得する。

**パラメータ**

- `command`: 2 文字のコマンド文字列

**戻り値**
`[移動前のインデックス, 移動後のインデックス]`

**処理内容**

1. 1 文字目を `parseIndex()` でインデックスに変換
2. 2 文字目を `parseIndex()` でインデックスに変換
3. 両インデックスを配列で返す

**処理例**

```java
int[] indexes = commandReader.parseCommand("AC");
// indexes = [0, 2]（A→0, C→2）

int[] indexes = commandReader.parseCommand("1N");
// indexes = [1, 13]（1→1, N→13）
```

#### `private boolean isValidCommand(String command)`

コマンドの妥当性を検証する。

**検証項目**

1. null チェック → null なら false
2. 長さ チェック → 2 文字以外なら false
3. 範囲チェック → 文字が A～N または 0～9 の範囲外なら false
4. 同一チェック → 1 文字目と 2 文字目が同じなら false

**パラメータ**

- `command`: 検証するコマンド文字列

**戻り値**

- `true`: すべての検証に合格
- `false`: いずれかの検証に不合格

**エラーメッセージ**

```
command = null: "2文字で入力して下さい。"
command = "A": "2文字で入力して下さい。"
command = "ABC": "2文字で入力して下さい。"
command = "AX": "A~Nの範囲で入力してください"
command = "AA": "1文字目と2文字目が同じ値です"
```

#### `private int parseIndex(char character)`

文字をジェムインデックスに変換する。

**パラメータ**

- `character`: 変換する文字

**戻り値**
インデックス値（0～13）。無効な文字の場合は -1。

**対応表**

```
A～N → 0～13
a～n → 0～13（大文字に変換して処理）
0～9 → 0～9
その他 → -1
```

**処理例**

```java
parseIndex('A') → 0
parseIndex('a') → 0（大文字に変換）
parseIndex('C') → 2
parseIndex('N') → 13
parseIndex('5') → 5
parseIndex('X') → -1
```

---

## InputProvider インターフェース

ユーザー入力を提供する抽象層。テスト用や別 UI への対応を容易にします。

### インターフェース概要

```java
public interface InputProvider
```

### メソッド

#### `String nextLine()`

1 行分の入力を取得。トリミング済み。

**戻り値**
ユーザーが入力した 1 行（先頭と末尾の空白が削除されている）

**実装例（標準入力）**

```
ユーザー入力: "  AC  "
戻り値: "AC"（トリミング済み）
```

#### `void close()`

入力ストリームを閉じる。

リソース解放用。通常は `try-with-resources` で自動呼び出し。

---

## SystemInInputProvider クラス

`System.in` から入力を取得する実装。

### クラス概要

```java
public class SystemInInputProvider implements InputProvider
```

### コンストラクタ

#### `public SystemInInputProvider()`

デフォルトの `Scanner(System.in)` を使用。

#### `public SystemInInputProvider(Scanner scanner)`

指定した `Scanner` を使用。

**パラメータ**

- `scanner`: 使用する `Scanner`

テスト時に別の入力源（ファイル、メモリバッファ等）に対応可能。

### メソッド

#### `@Override public String nextLine()`

1 行分の入力を取得（トリミング済み）。

**処理内容**

```
scanner.nextLine().trim()
```

**処理例**

```
標準入力: "太郎"
戻り値: "太郎"

標準入力: "  AC  "
戻り値: "AC"
```

#### `@Override public void close()`

`Scanner` を閉じて入力ストリームを終了する。

---

## OutputProvider インターフェース

出力を提供する抽象層。テスト用やファイル出力への対応を容易にします。

### インターフェース概要

```java
public interface OutputProvider
```

### メソッド

#### `void print(String text)`

テキストを出力（改行なし）。

**パラメータ**

- `text`: 出力するテキスト

#### `void println(String text)`

テキストを出力（改行あり）。

**パラメータ**

- `text`: 出力するテキスト

#### `void printf(String format, Object... args)`

フォーマット出力を行う。

**パラメータ**

- `format`: フォーマット文字列
- `args`: フォーマットの引数

**処理例**

```java
output.printf("HP=%d%n", 150);
// "HP=150\n" と出力
```

#### `void printColored(String text, String ansiColor)`

色付きテキストを出力（ANSI 制御コード使用、改行なし）。

**パラメータ**

- `text`: 出力するテキスト
- `ansiColor`: ANSI カラーコード

**ANSI カラーコード例**

```
31: 赤, 32: 緑, 33: 黄, 34: 青, 35: マゼンタ, 36: シアン
```

#### `void printlnColored(String text, String ansiColor)`

色付きテキストを出力（ANSI 制御コード使用、改行あり）。

**パラメータ**

- `text`: 出力するテキスト
- `ansiColor`: ANSI カラーコード

#### `void flush()`

フラッシュ（必要に応じて）。

バッファリングされた出力を即座に表示。

---

## StandardOutputProvider クラス

`System.out` へ出力する実装。

### クラス概要

```java
public class StandardOutputProvider implements OutputProvider
```

標準的なコンソール出力用。ANSI 色コードにも対応。

### メソッド

#### `@Override public void print(String text)`

テキストを出力（改行なし）。

**処理内容**

```
System.out.print(text);
```

#### `@Override public void println(String text)`

テキストを出力（改行あり）。

**処理内容**

```
System.out.println(text);
```

#### `@Override public void printf(String format, Object... args)`

フォーマット出力を行う。

**処理内容**

```
System.out.printf(format, args);
```

#### `@Override public void printColored(String text, String ansiColor)`

色付きテキストを出力（改行なし）。

**処理内容**

```
System.out.print("\u001B[" + ansiColor + "m" + text + "\u001B[0m");
```

ANSI シーケンスで色を設定し、テキスト後にリセット。

#### `@Override public void printlnColored(String text, String ansiColor)`

色付きテキストを出力（改行あり）。

**処理内容**

```
System.out.println("\u001B[" + ansiColor + "m" + text + "\u001B[0m");
```

#### `@Override public void flush()`

出力バッファをフラッシュする。

**処理内容**

```
System.out.flush();
```

---

## 使用例

### ゲーム開始時の入力

```java
InputProvider input = new SystemInInputProvider();
OutputProvider output = new StandardOutputProvider();
CommandReader commandReader = new CommandReader(input, output);

String playerName = commandReader.readPlayerName();
// "プレイヤー名を入力してください>" と表示
// ユーザーが "太郎" と入力
// playerName = "太郎"
```

### バトル中のコマンド入力

```java
String command = commandReader.readCommand();
// "コマンド？>" と表示
// ユーザーが "AC" と入力
// command = "AC"

int[] indexes = commandReader.parseCommand(command);
// indexes = [0, 2]
```

### テスト時の出力キャプチャ

```java
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
assertTrue(outputs.contains("＜パーティ編成＞"));
```
