Package **GameObject**

![Screen Shot 2020-12-05 at 5.59.24 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glddcazdflj30kb08pq36.jpg)

[GameObject Diagram PDF](diagram/GameObject.pdf)

AbstractGameObject is an abstract method which contains fields like xPosition, yPosition and abstract methods like render(), the subclasses which extends AbstractGameObject will realize these methods in their own way (render different Images), and implements other methods like eat() in Candy.

Movable is an interface provided to gain movable ablity to GameObjects, namely Crate and Player can move, and will realize methods like canMoveby() and MoveBy().

The GameObjectFactory here is a Factory that follows SimpleFactory Design Pattern which can construct different GameObjects in a uniform API.



Package **UI**

![Screen Shot 2020-12-06 at 8.48.14 PM](https://tva1.sinaimg.cn/large/0081Kckwly1glefc9tf1bj31f60jsac6.jpg) 

[UI Diagram PDF](diagram/UI.pdf)

UI package consists of the controllers for different views and sub-component of views, for example, the SoundPreferenceController is a sub-component that exists both in MenuView and GameView.

AbstractBarController is an abstract method which consist of the controllers for HighScoreBar and ColourPreference which has entry on the BottomBar, and the two view controllers, namely MenuViewController and GameViewController, will extend the AbstractBarController.

The GameView and MenuView class are the wrapper, which will read in FXML when constructing and contains getView() to return the JavaFX Pane loaded by FXML.



Package **Business**

![Screen Shot 2020-12-06 at 8.43.18 PM](/Users/kabylake/Desktop/Screen Shot 2020-12-06 at 8.43.18 PM.png)

[Business Diagram PDF](diagram/Business.pdf)

GameDocument has compositions like Level, which can be passed to GraphicRender to render the Map, the GameRecord has composition relation also with GameDocument to represents the records of the player, and the GameStageSaver's job is to save the current state of the Game. The GameGrid contains a grid of AbstractGameObjects, and can be iterated through.