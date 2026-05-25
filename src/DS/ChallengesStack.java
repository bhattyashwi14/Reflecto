package DS;

import Functionalities.Challenges;

public class ChallengesStack {

    int top, cap;
    Challenges.ChallengeRecord[] s;

    public ChallengesStack(int size){
        this.cap = size;
        this.top = -1;
        this.s = new Challenges.ChallengeRecord[size];
    }

    public void push(Challenges.ChallengeRecord ob){

        if(top == cap - 1){
            System.out.println("Stack Full.");
        }else{
            s[++top] = ob;
        }

    }

    public Challenges.ChallengeRecord peep(int i){

        int index = top - i + 1;

        if(index < 0 || index > cap-1){
            System.out.println("Invalid Index.");
            return null;
        }else{
            return s[index];
        }

    }

}