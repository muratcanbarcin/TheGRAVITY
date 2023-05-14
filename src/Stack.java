public class Stack {
    private int top;
    private char[] elements;

    public Stack(int n){
        elements = new char[n];
        top = -1;
    }

    public char peek(){
        if(isEmpty()) {
            System.out.println("Stack is empty");
            return '\0';
        }
        return elements[top];
    }

    public void push(char obj){
        if(isFull()) {
            System.out.println("Stack overflow");
            return;
        }
        elements[++top] = obj;
    }

    public char pop(){
        if(isEmpty()) {
            System.out.println("Stack is empty");
            return '\0';
        }
        char retData = elements[top];
        top--;
        return retData;
    }

    public boolean isEmpty(){
        return top == -1;
    }

    public boolean isFull(){
        return top + 1 == elements.length;
    }
    public int size(){
        return top + 1;
    }
}
