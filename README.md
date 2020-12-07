# 2020 COMP2059 Developing Maintainable Software Coursework

Zichen XU 20126507 hnyzx3@nottingham.edu.cn


>
> Word count: 496
>
> Table count: 5



[TOC]



### Refactor

- Split the GameObjects into several Classes (Wall, Crate, ..) with the inheritance from [AbstractGameObject](SokobanFX/src/Main/java/com/ae2dms/GameObject/AbstractGameObject.java), and a [Movable](SokobanFX/src/Main/java/com/ae2dms/GameObject/Movable.java) interface which Player and Crate can implement to gain moving ability. Enhance maintainability that future GameObjects can be easily added.
- Change the GameEngine to [GameDocument](SokobanFX/src/Main/java/com/ae2dms/Business/GameDocument.java) which behaves as Document job with methods like reloadStateFromFile() or serializeCurrentState() to adherent to Model in MVC pattern.
- The logic behind moving objects is down to the Player, who has [canMoveBy()](SokobanFX/src/Main/java/com/ae2dms/GameObject/Objects/Player.java#L113) to determine if can move by which direction and moveBy() to move. If a Crate is on the way, Player will 'ask' that Crate if the Crate canMoveBy(). This leaves the behaviour to specific GameObject, and controller just acts as the forwarder.
- Split the pop-ups into different FXMLs with its dedicated controller, and use FXML [\<include\>](SokobanFX/src/Main/resources/ui/FXML/GameViewScene.fxml#L73) to add nested controllers to MenuView / GameView



### Design Pattern:

- ##### Template Method

  AbstractGameObject has abstract render() method which different inheritance can have different implementations, but GraphicRender will only call [render()](SokobanFX/src/Main/java/com/ae2dms/Business/GraphicRender.java#L62) on different GameObjects despite different views they return.

  

- ##### Flyweight

  [ResourceFactory](SokobanFX/src/Main/java/com/ae2dms/IO/ResourceFactory.java#L137) will only load data once and can share this data for the next usage. for example, different Floors will have an identical Image to load when constructing.

  

- ##### Singleton

  The [MenuView](SokobanFX/src/Main/java/com/ae2dms/UI/Menu/MenuView.java#L49) will only be constructed once and reserved to be ready for every time returning to Menu.

  The [GameMediaPlayer](SokobanFX/src/Main/java/com/ae2dms/UI/GameMediaPlayer.java) as well.

  

- ##### SimpleFactory

  AbstractGameObject gives an identical constructor for all its inheritance, and all GameObjects can be constructed by GameObjectFactory with one single [API](SokobanFX/src/Main/java/com/ae2dms/GameObject/GameObjectFactory.java#L22).

  

- ##### Memento

  [GameStageSaver](SokobanFX/src/Main/java/com/ae2dms/Business/GameStageSaver.java) will save every state of GameDocument to the encoded string, and GameDocument offers a method to restore the state from another restored GameDocument.



### GUI Design Pattern

This project use MVC design pattern, in specific, [GameDocument](SokobanFX/src/Main/java/com/ae2dms/Business/GameDocument.java) as Model, who knows nothing about the view, FXML as View, and [GameViewController](SokobanFX/src/Main/java/com/ae2dms/UI/Game/GameViewController.java) as the controller, which mostly forward the changes to call methods in Model. However, in some fields, it may be more adherent to MVP (Presenter). The GraphicRender just put the render() in GridPane, but GameObject can change its behaviour (like [Animation](SokobanFX/src/Main/java/com/ae2dms/GameObject/Objects/Crate.java#L258)), and the view will change automatically without using Controller.



### Features

| Features                                                     | Screenshots                                                  | Comments|
| ------------------------------------------------------------ | ------------------------------------------------------------ |------ |
| Permanent High Score Listh with proper animation when opening from the bottomBar. | ![Screen Shot 2020-12-07 at 12.21.21 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6d2pl5oj312o0nwjwc.jpg) | |
| JUnit tests and Maven build files                            |                                                              | |
| Self re-designed UI from the ground up, with my Adobe XD sketch file available for reference | ![Screen Shot 2020-12-07 at 12.25.31 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6fivixdj315h0rfqdq.jpg) | [XD file link](Sokoban_Sketch.xd) |
| Re-position the button from MenuBar to MenuView and a separate Bottom-Bar. | ![Screen Shot 2020-12-07 at 12.29.35 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6jr9zy2j313g05twfw.jpg) |  |
| 2.5D view of the GameGrid with proper shelter effects | ![Screen Shot 2020-12-07 at 12.30.59 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6l7n958j305203pglq.jpg)![Screen Shot 2020-12-07 at 12.30.34 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6krs711j305003p74f.jpg) |  |
| Music controller which can change the volume, select songs to play and a useful progress slider, and will shuffle play by default | ![Screen Shot 2020-12-07 at 12.31.56 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6m7tx8vj30bh0h1acf.jpg) |  |
| Theme Preference pop-up to change the theme of the Crate and Diamond | ![Group 44](https://tva1.sinaimg.cn/large/0081Kckwly1glf6o5shcxj30p90e176c.jpg) |  |
| Eat Candy on the map and get a free Floor which can be drag and drop wherever you want on the Grid | ![Screen Shot 2020-12-07 at 12.34.56 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6pdjzapj305403c0su.jpg)![Screen Shot 2020-12-07 at 12.34.56 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6qq62pgj304603bjrw.jpg)![Screen Shot 2020-12-07 at 12.34.56 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6rwthwdj306g045t8x.jpg) |  |
| (Cheating) Click the Crate to select and move if the level is too difficult | ![Screen Shot 2020-12-07 at 12.38.31 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6t4d187j30qa08owfb.jpg) |  |
| Time and steps counter to count the duration and steps of each round, with sorted high score list either by time or by score | ![Screen Shot 2020-12-07 at 12.39.49 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6uigsepj307c01uaad.jpg)![Screen Shot 2020-12-07 at 12.40.25 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6v1bit6j30h009lmyu.jpg) |  |
| MapLoader can detect a invalid map file and points out which line is incorrect | ![Screen Shot 2020-12-07 at 12.42.05 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glf6ws25kaj30fe01q749.jpg) |  |


### Class Diagram and Explanations

refer to this folder [link](diagram)

### Git Usage

I strickly follow AngularJS Git Commit Message Conventions, the workflow can be concluded as: a branch for a large feature or bug fix, with sub-branches to handle the components and merge back.

### Test Cases

##### Business.Data.GameRecordTest

| Test ID| Test|Expected Outcome|Test Outcome|Modification|
|:----|:----|:----:|:----|:----|
| 1.1   | testRestoreRecordsFromEmpty | a empty Records list | Failed    | Fix  bug inside compareTo() in restoreRecords() |
| 1.1.1   |  | a empty Records list | Pass  |  |
| 1.2   | testWriteChangesToFileAndRestoreRecords | the sorted records with corrent size | Failed: the size does not match and | Fix bug in reading files from user.dir with incorrent Hashcode |
| 1.2.1   |  | the sorted records with corrent size | Pass |  |
| 1.3   | testSortRecords | the records with different sorting stradegies | Failed: the sortByTime() produce NullPointerException | fix the bug of comparison in sortRecordsByTime |
| 1.3.1 |  | the records with different sorting stradegies | Pass |  |


##### Business.Data.LevelTest

| Test ID| Test|Expected Outcome|Test Outcome|Modification|
|:----|:----|:----:|:----|:----|
| 2.1   | testRawLevelParsingMap1 | the Point inside all match | Failed: JavaFX media not initialzed exception | Set up new JFXPanel() beforeEach             |
| 2.1.1   |  | the Point inside all match | Pass  |  |
| 2.2   | testRawLevelParsingMap2 | the Point inside all match | Failed | Fix bug in parsing the whiteSpace |
| 2.2.1   |  | the Point inside all match | Pass |  |
| 2.3   | testRawLevelParsingMap3 | the Point inside all match | Pass |  |
| 2.4 | testMovement1 | the Point after movement all match | Failed: NullPointerException | modify Player to test whether it is on Candy |
| 2.4.1 |  | the Point after movement all match | Failed: NullPointerException | fix the X and Y axis in GameGrid to adherent to GameObject |
| 2.4.2 |  | the Point after movement all match | Pass |  |
| 2.5 | testMovement2 | the Point after movement all match | Pass |  |

##### Business.GameDocumentTest

| Test ID| Test|Expected Outcome|Test Outcome|Modification|
|:----|:----|:----:|:----|:----|
| 3.1   | testInitialization1 | the records, levels and Player match | Failed: NotSerilzable Exception | add transient to IntegerProperty in GameDocument |
| 3.1.1   |  | the records, levels and Player match | Pass  |  |
| 3.2   | testInitialization2 | the Point inside all match | Pass |  |
| 3.3   | testSerializeInitialState | the Point inside all match | Pass |  |
| 3.3.1 |  | the fields of the GameDocument after restored is identical to the initial state | Failed: records not identical | modify the restoreDocument to call restoreRecords after |
| 3.3.2 |  | the fields of the GameDocument after restored is identical to the initial state | Failed: NullPointerException | fix the transient property not initialised after restore |
| 3.3.3 |  | the fields of the GameDocument after restored is identical to the initial state | Pass |  |
| 3.4.1 | testUndo | the document with undo is identical to the one before | Failed: failed to undo becasue the stack in empty | add serializeCurrentState() to controller |
| 3.4.2 |  | the document with undo is identical to the one before | Failed: this.player is null | restore Player object with its image as well |
| 3.4.3 |  | the document with undo is identical to the one before | Pass |  |

##### Business.MapFileLoaderTest

| Test ID| Test|Expected Outcome|Test Outcome|Modification|
|:----|:----|:----:|:----|:----|
| 4.1   | testValidMap1 | Map is Valid | Pass |  |
| 4.2  | testValidMap2 | Map is Valid | Failed | the RegEx does not match the desired output               |
| 4.2.1 |                 | Map is Valid | Pass |  |
| 4.3   | testValidMap3 | Map is Valid | Pass |  |
| 4.3.1 | testInvalidMap1 | Map is invalid | Failed: line number is not correct | create a ModifiedBufferReader to add a customzied pointer |
| 4.3.2 |  | Map is invalid | Failed: arrayOutOfBoundException | fix the index our of array bound |
| 4.3.3 |  | Map is invalid | Pass |  |
| 4.4 | testInvalidMap2 | Map is invalid | Pass | add serializeCurrentState() to controller |
| 4.5 | testInvalidMap3 | Map is invalid | Failed: not match | the RegEx still does not match the desired output, add ^ and $ |
| 4.5.1 |                 | Map is invalid | Pass |  |
| 4.6 | testInvalidMap4 | Map is invalid | Pass |  |

##### UI.HighScoreBar.HighScoreBarControllerTest

| Test ID| Test|Expected Outcome|Test Outcome|Modification|
|:----|:----|:----:|:----|:----|
| 4.1   | testRender1 | The VBox in controller matches the data in records | Failed: JavaFX media not initialzed exception | Set up new JFXPanel() beforeEach |
| 4.1.1 | testRender1 | The VBox in controller matches the data in records | Failed: the compare methods throws warning | Change the Compatator's return value to 1, -1 and 0 |
| 4.1.2   | testRender1 | The VBox in controller matches the data in records | Pass |  |







