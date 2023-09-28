package JavaFX_Snake_Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.BLUE;

public class SnakeGame extends Application {
    final int size=500, dot_size=10, up=1, right=2, down=3, left=4;
    int  length=10, dir=2, food_x, food_y;//2 is for axis of direction(x,y)
    Canvas canvas;
    GraphicsContext gc;
    int x[]=new int[size*size];//length of x axis
    int y[]=new int[size*size];//length of y axis
    Thread game;//creation of thread
    boolean lost=false;//the false is to be need for not to take random input

    StackPane sp;

    //@Override
    public void start(Stage s)
    {


        //b1.setOnAction((EventHandler<ActionEvent>) this);

        //b2.setOnAction();
        StackPane sp = new StackPane();    //layout is created
        canvas=new Canvas(size,size);//   size of the game's Scene(x,y)
        gc=canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        sp.getChildren().add(canvas);


        startGame();//calling a start game
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent e)
            {
                KeyCode key=e.getCode();//to get input key in the keyboard
                if(key.equals(KeyCode.UP))
                    dir=up;
                if(key.equals(KeyCode.DOWN))
                    dir=down;
                if(key.equals(KeyCode.LEFT))
                    dir=left;
                if(key.equals(KeyCode.RIGHT))
                    dir=right;
            }

        });
        Scene scene = new Scene(sp, size, size);
        s.setTitle("SNAKE GAME");
        s.setScene(scene);
        s.show();
    }//program ends
    private void draw(GraphicsContext gc)//a jar present
    {
        gc.clearRect(0, 0, size, size);
        if(!lost)
        {
            gc.setFill(Paint.valueOf("green"));//for food color
            gc.fillOval(food_x, food_y, dot_size, dot_size);
            gc.setFill(Paint.valueOf("red"));//for snake head color
            gc.fillOval(x[0], y[0], dot_size, dot_size);
            gc.setFill(Paint.valueOf("orange"));//for remaining body parts of the snake
            for(int i=1; i<length; i++)//the length of snake increases with a color
            {
                gc.fillOval(x[i], y[i], dot_size, dot_size);
            }

        }
        else
        {
            //gc.setFill(Paint.valueOf("red"));
            // by the two statements we can set the colors      1
            gc.setFont(Font.font("Abyssinia SIL", FontWeight.EXTRA_BOLD, FontPosture.REGULAR ,25));//two types are italic and regular
            gc.setFill(BLUE);// setting colour of the text to blue
            gc.fillText("Game Over", size/2-50, size/2-50);
            game.stop();//used top stop the execution
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void startGame()
    {
        length=3;//default size
        for(int i=0; i<length; i++){
            x[i]=50-i*dot_size;
            y[i]=50;//started apart
        }
        locateFood();
        game=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    if(!lost)
                    {
                        checkFood();//function call
                        checkCollision();//function call
                        move();// function call
                    }
                    draw(gc);//function calling
                    try
                    {
                        Thread.sleep(75);
                    }
                    catch(Exception e)
                    {
                    }
                }
            }
        }
        );
        game.start();//call the start method in thread
    }

    private void locateFood() //this line is used for generating the random foods
    {
        food_x=(int)(Math.random()*((size/dot_size)-1))*dot_size;
        food_y=(int)(Math.random()*((size/dot_size)-1))*dot_size;
    }
    private void checkFood() // it is used to increment the size of the snake with the help of the food
    {
        if(x[0]==food_x && y[0]==food_y)
        {
            length++;
            locateFood();
        }
    }
    private void checkCollision()// it is used to print game over when we out
    {
        if(x[0]>=size)
            lost=true;
        if(y[0]>=size)
            lost=true;
        if(x[0]<0)
            lost=true;
        if(y[0]<0)
            lost=true;
        for(int i=3; i<length; i++)
        {
            if (x[0] == x[i] && y[0] == y[i])
            {
                lost = true;
            }
        }
    }
    private void move()
    {
        for(int i=length-1;i>0;i--)
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        if(dir==up)y[0]-=dot_size;
        if(dir==down)y[0]+=dot_size;
        if(dir==right)x[0]+=dot_size;
        if(dir==left)x[0]-=dot_size;
    }
}/*move(),checkCollision(),checkFood(),locateFood() */