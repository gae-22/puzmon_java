# Puzzle & Monsters システム図解

このドキュメントは、Puzzle & Monsters ゲームの各種システムを図で可視化しています。

## 目次

- [Puzzle \& Monsters システム図解](#puzzle--monsters-システム図解)
    - [目次](#目次)
    - [クラス構成図](#クラス構成図)
    - [クラス依存関係図](#クラス依存関係図)
    - [バトルフロー図](#バトルフロー図)
    - [ジェムマッチプロセス](#ジェムマッチプロセス)
    - [ダメージ計算フロー](#ダメージ計算フロー)
    - [ゲーム初期化シーケンス](#ゲーム初期化シーケンス)
    - [単一ターン処理シーケンス](#単一ターン処理シーケンス)
    - [属性相性図](#属性相性図)
    - [相性倍率マトリックス図](#相性倍率マトリックス図)
    - [パーティHP管理](#パーティhp管理)
    - [コンボシステム](#コンボシステム)

---

## クラス構成図

```mermaid
classDiagram
    class Main {
        -OutputProvider output
        -CommandReader commandReader
        +main(String[] args) void
    }

    class Party {
        -String name
        -List~Monster~ friends
        -GemBoard gemBoard
        -int hp
        -int maxHp
        -int averageDefense
        +getName() String
        +getFriends() List
        +getTotalHp() int
        +getMaxHp() int
        +takeDamage(int damage) void
        +heal(int amount) void
        +isDefeated() boolean
        +findFriendByElement(Element) Monster
    }

    class Monster {
        -String name
        -int maxHp
        -int hp
        -Element element
        -int attackPower
        -int defensePower
        +getName() String
        +getHp() int
        +getMaxHp() int
        +getElement() Element
        +getAttackPower() int
        +getDefensePower() int
        +takeDamage(int damage) void
        +heal(int amount) void
        +isDead() boolean
    }

    class GemBoard {
        -Element[] gems
        +initialize() void
        +getGems() Element[]
        +swapGems(int, int) void
        +moveGems(int, int, boolean) void
        +findMatchIndex() int
        +getMatchCount(int) int
        +clearGems(int, int) void
        +shiftGems() void
        +spawnGems() void
        +hasEmptyCells() boolean
    }

    class BattleManager {
        -Party party
        -Monster enemy
        -CommandReader commandReader
        -DamageCalculator damageCalculator
        -OutputProvider output
        +battle() boolean
        +playerTurn() void
        +enemyTurn() void
        +evaluateGems() void
        -doAttack(Monster, int, int) void
        -doRecover(int, int) void
    }

    class Element {
        <<enumeration>>
        FIRE
        WIND
        EARTH
        WATER
        LIFE
        NONE
        +getDisplayName() String
        +getSymbol() String
        +isPlayable() boolean
        +randomPlayable(Random) Element
    }

    class DamageCalculator {
        -ElementBoostMatrix boostMatrix
        +getBaseDamage(int, int) int
        +getElementBoost(Element, Element) double
        +getComboBoost(int, int) double
        +getRandomFactor() double
        +calculateFinalDamage(...) int
    }

    class ElementBoostMatrix {
        -Map boostMatrix
        +getBoost(Element, Element) double
    }

    class CommandReader {
        -InputProvider inputProvider
        -OutputProvider output
        +readPlayerName() String
        +readCommand() String
        +parseCommand(String) int[]
        -isValidCommand(String) boolean
        -parseIndex(char) int
    }

    class Display {
        <<utility>>
        -OutputProvider output
        +setOutputProvider(OutputProvider) void
        +showPartyInfo(Party) void
        +showBattleField(Party, Monster) void
        +showGems(GemBoard) void
        +showMonsterName(Monster) void
    }

    class GameData {
        <<constant>>
        GEMS_LENGTH
        PARTY_SIZE
        LINE_LENGTH
        INITIAL_GEMS[]
        +createEnemies() Monster[]
        +createPlayers() Monster[]
    }

    class InputProvider {
        <<interface>>
        +nextLine() String
        +close() void
    }

    class OutputProvider {
        <<interface>>
        +print(String) void
        +println(String) void
        +printf(String, Object...) void
        +printColored(String, String) void
    }

    Main --> CommandReader
    Main --> Party
    Main --> BattleManager
    Main --> Display
    Main --> GameData

    Party --> Monster
    Party --> GemBoard

    GemBoard --> Element

    BattleManager --> Party
    BattleManager --> Monster
    BattleManager --> CommandReader
    BattleManager --> DamageCalculator
    BattleManager --> Display

    DamageCalculator --> Element
    DamageCalculator --> ElementBoostMatrix

    ElementBoostMatrix --> Element

    CommandReader --> InputProvider
    CommandReader --> OutputProvider

    Display --> Party
    Display --> Monster
    Display --> GemBoard
    Display --> Element
    Display --> OutputProvider
```

---

## クラス依存関係図

```mermaid
graph TB
    Main["Main<br/>(エントリーポイント)"]

    Main -->|初期化| CR["CommandReader<br/>(入力管理)"]
    Main -->|初期化| Party["Party<br/>(パーティ)"]
    Main -->|初期化| BM["BattleManager<br/>(バトル管理)"]
    Main -->|初期化| Disp["Display<br/>(表示)"]
    Main -->|初期化| GD["GameData<br/>(ゲームデータ)"]

    Party -->|構成| Monster1["Monster<br/>(プレイヤー)"]
    Party -->|管理| GB["GemBoard<br/>(ジェムボード)"]

    BM -->|バトル対象| Monster2["Monster<br/>(敵)"]
    BM -->|使用| Party
    BM -->|ジェム操作| GB
    BM -->|ダメージ計算| DC["DamageCalculator<br/>(ダメージ計算)"]

    DC -->|属性相性| EBM["ElementBoostMatrix<br/>(相性行列)"]

    GB -->|要素| Elem["Element<br/>(属性enum)"]
    Monster1 -->|属性| Elem
    Monster2 -->|属性| Elem

    CR -->|入力| IP["InputProvider<br/>(入力インタフェース)"]
    Disp -->|出力| OP["OutputProvider<br/>(出力インタフェース)"]

    style Main fill:#ff9999
    style Party fill:#99ccff
    style Monster1 fill:#99ff99
    style Monster2 fill:#ffcccc
    style BM fill:#ffcc99
    style GB fill:#ccccff
    style DC fill:#ffff99
    style Elem fill:#99ffcc
```

---

## バトルフロー図

```mermaid
graph TD
    Start["ゲーム開始"]

    Input["プレイヤー名入力"]
    CreateParty["パーティ作成<br/>4体のモンスター"]
    ShowPartyInfo["パーティ情報表示"]

    Start --> Input
    Input --> CreateParty
    CreateParty --> ShowPartyInfo

    EnemyLoop{{"敵が<br/>残っているか？"}}
    ShowPartyInfo --> EnemyLoop

    EnemyCreate["敵モンスター出現"]
    BattleStart["バトル開始"]

    EnemyLoop -->|YES| EnemyCreate
    EnemyCreate --> BattleStart

    PlayerTurn["【プレイヤーターン】<br/>・ジェム移動入力<br/>・マッチ判定<br/>・攻撃/回復処理"]

    BattleStart --> PlayerTurn

    EnemyDead{{"敵が<br/>戦闘不能？"}}
    PlayerTurn --> EnemyDead

    Victory["敵撃破！"]
    EnemyDead -->|YES| Victory

    EnemyTurn["【敵ターン】<br/>・ダメージ計算<br/>・パーティにダメージ"]
    EnemyDead -->|NO| EnemyTurn

    PartyDefeated{{"パーティ<br/>全滅？"}}
    EnemyTurn --> PartyDefeated

    GameOver["ゲームオーバー"]
    PartyDefeated -->|YES| GameOver

    PartyDefeated -->|NO| PlayerTurn

    Victory --> EnemyLoop

    AllEnemies{{"全敵<br/>撃破？"}}
    EnemyLoop -->|NO| AllEnemies
    AllEnemies -->|NO| EnemyCreate

    Clear["ゲームクリア！"]
    AllEnemies -->|YES| Clear

    Result["結果表示<br/>・倒したモンスター数"]
    GameOver --> Result
    Clear --> Result

    style Start fill:#ccffcc
    style Clear fill:#ccffcc
    style GameOver fill:#ffcccc
    style Victory fill:#ffffcc
    style PlayerTurn fill:#cce5ff
    style EnemyTurn fill:#ffe5cc
```

---

## ジェムマッチプロセス

```mermaid
graph TD
    Start["マッチ評価開始"]

    Find["3個以上連続するジェムを探す<br/>findMatchIndex()"]
    Start --> Find

    Found{{"マッチした<br/>ジェム発見？"}}
    Find --> Found

    Done["マッチ評価終了"]
    Found -->|NO| Done

    GetCount["マッチ数を取得<br/>getMatchCount()"]
    Found -->|YES| GetCount

    GetElem["ジェムの属性を取得"]
    GetCount --> GetElem

    Clear["ジェムを削除<br/>clearGems()"]
    GetElem --> Clear

    IsLife{{"属性が命<br/>LIFE？"}}
    Clear --> IsLife

    Recover["回復処理<br/>doRecover()"]
    IsLife -->|YES| Recover

    IsLife -->|NO| Attack["攻撃処理<br/>doAttack()"]

    Recover --> HasEmpty
    Attack --> HasEmpty

    HasEmpty{{"空きセル<br/>存在？"}}

    Shift["ジェムをシフト<br/>shiftGems()"]
    HasEmpty -->|YES| Shift

    Spawn["新ジェムを生成<br/>spawnGems()"]
    Shift --> Spawn

    HasEmpty -->|NO| Find
    Spawn --> Find

    style Done fill:#ccffcc
    style Recover fill:#ccffff
    style Attack fill:#ffcccc
```

---

## ダメージ計算フロー

```mermaid
graph LR
    Attack["攻撃力<br/>Atk"]
    Defense["防御力<br/>Def"]

    Attack --> BaseDmg["基本ダメージ<br/>Base = Atk - Def<br/>最小値: 0"]
    Defense --> BaseDmg

    AtkElem["攻撃側属性"]
    DefElem["防御側属性"]

    AtkElem --> ElementBoost["属性相性倍率<br/>Eff ∈ {0.5, 1.0, 2.0}"]
    DefElem --> ElementBoost

    GemCount["マッチ数<br/>Gems"]
    ComboCount["コンボ数<br/>Combo"]

    GemCount --> ComboBoost["コンボ倍率<br/>Combo = 1.5^(Gems-3+Combo)"]
    ComboCount --> ComboBoost

    RandomVal["ランダム値<br/>0.9 ~ 1.1"]
    RandomVal --> RandomFactor["ランダム因子<br/>Random"]

    BaseDmg --> Final["最終ダメージ計算<br/>Final = Base × Eff × Combo × Random"]
    ElementBoost --> Final
    ComboBoost --> Final
    RandomFactor --> Final

    Final --> Result["最終ダメージ<br/>max(1, ⌊Final⌋)"]

    style Result fill:#ffcccc
    style Final fill:#ffff99
```

---

## ゲーム初期化シーケンス

```mermaid
sequenceDiagram
    participant User
    participant Main
    participant CommandReader
    participant Display
    participant GameData
    participant Party
    participant Monster

    User->>Main: java Main

    activate Main
    Main->>CommandReader: new CommandReader()

    Note over Main: プレイヤー名入力
    Main->>CommandReader: readPlayerName()
    CommandReader->>User: "プレイヤー名を入力してください>"
    User->>CommandReader: "太郎"
    CommandReader-->>Main: "太郎"

    Note over Main: パーティ作成
    Main->>GameData: createPlayers()
    GameData-->>Monster: 4体のモンスターを生成
    Monster-->>GameData: Monster[]
    GameData-->>Main: Monster[]

    Main->>Party: new Party("太郎", players)
    activate Party
    Party->>Monster: getHp(), getMaxHp() etc.
    Party-->>Main: Party インスタンス
    deactivate Party

    Note over Main: パーティ情報表示
    Main->>Display: showPartyInfo(party)
    Display->>Party: getFriends()
    Display->>User: 朱雀 HP=150 攻撃=30...

    Note over Main: 敵作成
    Main->>GameData: createEnemies()
    GameData-->>Monster: 5体の敵を生成
    Monster-->>GameData: Monster[]
    GameData-->>Main: Monster[]

    Note over Main: バトルループ準備完了

    deactivate Main
```

---

## 単一ターン処理シーケンス

```mermaid
sequenceDiagram
    participant User
    participant BattleManager
    participant Party
    participant GemBoard
    participant CommandReader
    participant Display
    participant DamageCalculator
    participant Enemy

    Note over BattleManager: プレイヤーターン
    BattleManager->>Display: showBattleField(party, enemy)
    Display-->>User: バトル画面を表示

    BattleManager->>CommandReader: readCommand()
    CommandReader->>User: "コマンド？>"
    User->>CommandReader: "AB"
    CommandReader-->>BattleManager: "AB"

    BattleManager->>CommandReader: parseCommand("AB")
    CommandReader-->>BattleManager: [0, 1]

    BattleManager->>GemBoard: moveGems(0, 1, true)
    GemBoard-->>Display: ジェム移動のアニメーション

    BattleManager->>GemBoard: findMatchIndex()
    GemBoard-->>BattleManager: matchIndex

    alt マッチあり
        BattleManager->>GemBoard: getMatchCount(index)
        GemBoard-->>BattleManager: count

        BattleManager->>GemBoard: clearGems(index, count)
        GemBoard-->>Display: ジェム消除表示

        BattleManager->>BattleManager: doAttack(friend, count, combo)

        activate BattleManager
        BattleManager->>DamageCalculator: calculateFinalDamage()
        DamageCalculator-->>BattleManager: damage
        BattleManager->>Enemy: takeDamage(damage)
        deactivate BattleManager

        BattleManager->>GemBoard: hasEmptyCells()
        alt 空きセルあり
            BattleManager->>GemBoard: shiftGems()
            BattleManager->>GemBoard: spawnGems()
        end
    else マッチなし
        Note over BattleManager: マッチ評価終了
    end

    Note over BattleManager: 敵ターン
    BattleManager->>DamageCalculator: calculateRandomFactor()
    BattleManager->>Party: takeDamage(damage)
    Display->>User: ダメージメッセージ表示
```

---

## 属性相性図

```mermaid
graph TB
    Fire["火"]
    Wind["風"]
    Earth["土"]
    Water["水"]
    Life["命"]

    Fire -->|強い| Wind
    Wind -->|強い| Earth
    Earth -->|強い| Fire

    Fire -->|弱い| Earth
    Wind -->|弱い| Fire
    Earth -->|弱い| Wind

    Water -->|等倍| Fire
    Water -->|等倍| Wind
    Water -->|等倍| Earth
    Water -->|等倍| Water

    Life -->|等倍| Fire
    Life -->|等倍| Wind
    Life -->|等倍| Earth
    Life -->|等倍| Water

    style Fire fill:#ff9999
    style Wind fill:#99ccff
    style Earth fill:#ffcc99
    style Water fill:#99ff99
    style Life fill:#ff99ff
```

---

## 相性倍率マトリックス図

```mermaid
graph LR
    subgraph Attack["攻撃属性"]
        A1["火"]
        A2["風"]
        A3["土"]
        A4["水"]
    end

    subgraph Vs["vs"]
    end

    subgraph Def["防御属性"]
        D1["火"]
        D2["風"]
        D3["土"]
        D4["水"]
    end

    subgraph Result["ダメージ倍率"]
        R1["1.0"]
        R2["2.0"]
        R3["0.5"]
        R4["1.0"]
        R5["0.5"]
        R6["1.0"]
        R7["2.0"]
        R8["1.0"]
        R9["2.0"]
        R10["0.5"]
        R11["1.0"]
        R12["1.0"]
        R13["1.0"]
        R14["1.0"]
        R15["1.0"]
        R16["1.0"]
    end

    A1 --> R1
    A1 --> R2
    A1 --> R3
    A1 --> R4
    A2 --> R5
    A2 --> R6
    A2 --> R7
    A2 --> R8
    A3 --> R9
    A3 --> R10
    A3 --> R11
    A3 --> R12
    A4 --> R13
    A4 --> R14
    A4 --> R15
    A4 --> R16
```

---

## パーティHP管理

```mermaid
graph TB
    subgraph PartyHP["Party総HP"]
        P1["朱雀: 150"]
        P2["青龍: 150"]
        P3["白虎: 150"]
        P4["玄武: 150"]
        Total["合計: 600"]
    end

    Damage["敵ターン<br/>ダメージ発生"]
    Reduce["Party.takeDamage()"]
    Heal["回復ジェムマッチ<br/>Party.heal()"]

    Total --> Damage
    Damage --> Reduce
    Reduce --> Check{{"HP ≤ 0？"}}
    Check -->|YES| GameOver["全滅<br/>ゲームオーバー"]
    Check -->|NO| Continue["継続"]

    Continue --> Heal
    Heal --> Total

    style Total fill:#ffcccc
    style GameOver fill:#ff9999
    style Continue fill:#ccffcc
```

---

## コンボシステム

```mermaid
graph TB
    Turn["【1ターン内での処理】"]

    Turn --> First["1回目のマッチ<br/>コンボ = 0"]
    First --> Calc1["ダメージ倍率 = 1.5^(3-3+0) = 1.0"]
    Calc1 --> Attack1["攻撃実行"]

    Attack1 --> Chain1{{"新しいマッチ<br/>発生？"}}

    Chain1 -->|YES| Second["2回目のマッチ<br/>コンボ = 1"]
    Second --> Calc2["ダメージ倍率 = 1.5^(3-3+1) = 1.5"]
    Calc2 --> Attack2["攻撃実行（1.5倍）"]

    Chain1 -->|NO| End["ターン終了<br/>総ダメージ = 基本のみ"]

    Attack2 --> Chain2{{"新しいマッチ<br/>発生？"}}

    Chain2 -->|YES| Third["3回目のマッチ<br/>コンボ = 2"]
    Third --> Calc3["ダメージ倍率 = 1.5^(3-3+2) = 2.25"]
    Calc3 --> Attack3["攻撃実行（2.25倍）"]

    Chain2 -->|NO| End

    Attack3 --> End

    style Attack1 fill:#ffcccc
    style Attack2 fill:#ffddaa
    style Attack3 fill:#ffeeaa
    style End fill:#ccffcc
```
