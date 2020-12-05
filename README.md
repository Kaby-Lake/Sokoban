# 2020 COMP2059 Develping Maintainable Software Coursework

#### Refactor

- Modify the GameObjects into several Classes (Wall, Crate, ..) with inheritance from AbstractGameObject, and offer a Moveable interface which Player and Crate can implements to gain movement ability. Enhance mainability that future GameObjects can be easily added
- Change the GameEngine to GameDocument, which behaves like a Document job by adding methods like reloadStateFromFile() or serializeCurrentState()  to adherent to Model in MVC patten.
- The underlying logic behind moving objects is down to the Player's work, Player has canMoveBy() to determine if can move by which dierection and moveBy() to move. If there is Crate on the way, Player will 'ask' this Crate if it canMoveBy(). This leave the behavior to specific GameObject and controller just acts as forwarder.
- Split the pop ups into different FXMLs with its own dedicated controller, and will be dynamicly added in GamView or MenuView.



#### Design Pattern:

- ##### Template Method

  AbstractGameObject has abstract render() method which different inheritance can have different implementation, but GraphicRender will only call render() on different GameObjects dispite of different views they returns.

  

- ##### Flyweight

  ResourceFactory will only load a data once, and can share this data for the next usage. for exmaple different Floors will have the identical Image to load when constructing.

  

- ##### Singleton

  The MenuView will only be constructed once, and reserved to be ready for everytime returing to Menu.

  The GameMediaPlayer as well.

  

- ##### SimpleFactory

  AbstractGameObject gives a identical constructer for all its inheritance, and all GameObjects can be constructed by GameObjectFactory with one single API.

  

- ##### Memento

  GameStageSaver will save every state of GameDocument at its change to encoded string, and GameDocument offers a method to restore the state from another restored GameDocument.



#### GUI Design Pattern

This project use MVC design pattern, in specific, GameDocument as Model, which knows nothing about the view, FXML as View, and GameController as controller, which mostly forward the changes to call methods in Model. However in some fields, it may be more adherent to MVP (Presenter). The GraphicRender just put the return value of render() in GridPane, but GameObject can change its behavior (like Animation) and the view will change automatically without the Controller.



#### Fetures

- Self re-desgined UI from the ground up, with my Adobe XD sketch file available for reference.
- Re-position the button from MenuBar to MenuView and a separate ButtomBar.
- 2.5D view of the GameGrid with proper shelter effects.
- Music controller which can toggle the volumn, select songs to play and a useful progress slider, and will shuffle play by default.
- Theme Preference pop up to change the theme of the Crate and Diamond.
- Eat Candy on map and get a free Floor which can be drag and drop wherever you want on the Grid.
- (Cheating) Click the Crate to select and move if level is too difficult.
- Time and steps counter to count the duration and steps of each round, with sorted high score list either by time or by score. has proper animation when opening High Score List from the bottomBar.

