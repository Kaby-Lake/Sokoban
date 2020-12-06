# 2020 COMP2059 Developing Maintainable Software Coursework

#### Refactor

- Split the GameObjects into several Classes (Wall, Crate, ..) with the inheritance from AbstractGameObject, and a Moveable interface which Player and Crate can implement to gain moving ability. Enhance maintainability that future GameObjects can be easily added.
- Change the GameEngine to GameDocument which behaves as Document job with methods like reloadStateFromFile() or serializeCurrentState() to adherent to Model in MVC pattern.
- The logic behind moving objects is down to the Player, who has canMoveBy() to determine if can move by which direction and moveBy() to move. If a Crate is on the way, Player will 'ask' that Crate if the Crate canMoveBy(). This leaves the behaviour to specific GameObject, and controller just acts as the forwarder.
- Split the pop-ups into different FXMLs with its dedicated controller, and use FXML <include> to add nested controllers to MenuView / GameView



#### Design Pattern:

- ##### Template Method

  AbstractGameObject has abstract render() method which different inheritance can have different implementations, but GraphicRender will only call render() on different GameObjects despite different views they return.

  

- ##### Flyweight

  ResourceFactory will only load data once and can share this data for the next usage. for example, different Floors will have an identical Image to load when constructing.

  

- ##### Singleton

  The MenuView will only be constructed once and reserved to be ready for every time returning to Menu.

  The GameMediaPlayer as well.

  

- ##### SimpleFactory

  AbstractGameObject gives an identical constructor for all its inheritance, and all GameObjects can be constructed by GameObjectFactory with one single API.

  

- ##### Memento

  GameStageSaver will save every state of GameDocument to the encoded string, and GameDocument offers a method to restore the state from another restored GameDocument.



#### GUI Design Pattern

This project use MVC design pattern, in specific, GameDocument as Model, who knows nothing about the view, FXML as View, and GameController as the controller, which mostly forward the changes to call methods in Model. However, in some fields, it may be more adherent to MVP (Presenter). The GraphicRender just put the render() in GridPane, but GameObject can change its behaviour (like Animation), and the view will change automatically without using Controller.



#### Features
- permanent High Score List, JUnit tests and Maven build files.
- Self re-designed UI from the ground up, with my Adobe XD sketch file available for reference.
- Re-position the button from MenuBar to MenuView and a separate Bottom-Bar.
- 2.5D view of the GameGrid with proper shelter effects.
- Music controller which can change the volume, select songs to play and a useful progress slider, and will shuffle play by default.
- Theme Preference pop-up to change the theme of the Crate and Diamond.
- Eat Candy on the map and get a free Floor which can be drag and drop wherever you want on the Grid.
- (Cheating) Click the Crate to select and move if the level is too difficult.
- Time and steps counter to count the duration and steps of each round, with sorted high score list either by time or by score. has proper animation when opening High Score List from the bottomBar.
- MapLoader can detect a invalid map file and points out which line is incorrect.


#### Class Diagram and Explanations

refer to this folder link

