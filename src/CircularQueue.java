public class CircularQueue {
    private int rear, front;
    private char[] elements;

    public CircularQueue (int capacity)
    {
        elements = new char[capacity];
        rear = -1;
        front = 0;
    }

    public void enqueue (char data) {
        if (isFull())

            System.out.println("Queue overflow");

        else {

            rear = (rear + 1) % elements.length;
            elements [rear]=data;
        }

    }
    public char dequeue () {
        if (isEmpty())
        {
            System.out.println("Queue is empty");
            return '\0';
        }
        else
        {
            char retData=elements [front];
            elements [front]= '\0';
            front=(front + 1) % elements.length;
            return retData;
        }
    }

    public char peek() {

        if (isEmpty()) {

            System.out.println("Queue is empty");
            return '\0';
        }
        else
        {
            return elements[front];

        }
    }

    public boolean isEmpty() {

        return elements [front]== '\0';

    }

    public boolean isFull() {

        return (front==(rear + 1) % elements.length && elements [front] != '\0' && elements [rear] != '\0');


    }

    public int size() {

        if (elements [front]=='\0') {

            return 0;
        }

        else if (rear>= front) {

            return rear-front+1;

        }

        else {
            return elements.length-(front-rear)+1;
        }

    }



}