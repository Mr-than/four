/*
 这个是在第一次作业的最高难度的基础上做的修改（或者可以说是增强？）
  之所以说是修改和增强，是因为这里的arraylist是直接复制过来的，而不是
  重新写的一份，因为貌似这次作业想考察的并不是集合的实现，而是以下

 泛型
 接口，重写的概念
 类集的迭代输出
 链表的概念
 内部类

 所以重写一份无异于做无用功。

 这里的迭代输出是根据我的理解手撸，可能和真实情况不相符合


 下面有些地方是我还没来得及优化的shit代码，希望不要辣了学长的眼睛/头秃

 */

import java.util.Iterator;

public class
MyArrayList<E> implements Iterable<E> {
    private MyLink<E> it = new MyLink<>();
    private MyLinkPlus<E> itp = new MyLinkPlus<>();


    private int size = 0;
    //object数组
    private Object[] elementDate;

    {
        elementDate = new Object[10];
    }


    public int getSize() {
        return this.size;
    }

    //实现扩容
    public void add(E obj) {

        if (size == elementDate.length) {
            int a = elementDate.length;
            Object[] temp = new Object[(int) (a * 1.5)];
            for (int i = 0; i < a; ++i) {
                temp[i] = this.elementDate[i];
            }
            elementDate = temp;
        }
        elementDate[size] = obj;
        it.add(obj);
        itp.add(obj);
        size++;
    }

    //get方法
    public Object get(int num) {
        if (num < size) {
            return elementDate[num];
        }

        throw new RuntimeException("数组越界了");
    }

    //remove方法
    public void remove(int index) {
        if (index < size) {
            for (int i = index; i < size - 1; ++i) {
                elementDate[i] = elementDate[i + 1];
            }
            it.disable(index);
            itp.disable(index);
            size--;
        }
        if (index > size) {
            throw new RuntimeException("数组越界了");
        }
    }

    //remove方法的重载
    public void remove(E index) {
        for (int i = 0; i < size; ++i) {
            if (index == elementDate[i]) {
                for (int a = i; a < size - 1; ++i) {
                    elementDate[a] = elementDate[a + 1];
                }
                return;
            }
        }
        System.out.println("无此元素");

    }

    //add重载
    public void add(int index, E obj) {
        /*
         * 传入后判断，如果数组长度能装下index+1个元素，则直接装入，如果不能，则扩容后装入
         *
         * size<index+1
         * size==index+1
         * size>index+1
         *
         * */

        if (size == elementDate.length) {
            int a = elementDate.length;
            Object[] temp = new Object[(int) (a * 1.5)];
            for (int i = 0; i < a; ++i) {
                temp[i] = this.elementDate[i];
            }
            elementDate = temp;
        }
        if (size == 0 && index == 0) {
            elementDate[0] = obj;
            it.add(index,obj);
            itp.add(index,obj);
            this.size++;
            return;//tmd这里不加个return他size会变成3，懒得排下面了，啧
        }
        if (index > size && index != 0) {
            throw new RuntimeException("下标超过了数组长度");
        }
        if (index < size) {
            Object last = elementDate[size - 1];
            for (int i = size; i > index + 1; i--) {
                elementDate[i - 1] = elementDate[i - 2];
            }
            elementDate[size] = last;
            elementDate[index] = obj;
            it.add(index,obj);
            itp.add(index,obj);
            size++;
        }
    }


    //迭代器的方法重写
    public Iterator<E> iterator() {

        return new Iterator<E>() {
            MyLink<E>.Node m = it.re();
            MyLink<E>.Node temp;

            @Override
            public boolean hasNext() {
                temp = m;
                if (this.m != null) {
                    this.m = this.m.next;
                    return true;
                }
                return false;
            }

            @Override
            public E next() {
                return this.temp.t;
            }
        };
    }


    //以下，实现迭代器的链表部分（完全按照自己的想法来的，可能会和真实的迭代方法有一定出入，当然我以后一定会加强对源码的了解）

    private class MyLink<E> {
        //下面定义一个变量来存储链表的长度，每添加一个加一
        private int size = 0;

        private Node first;
        private Node last;

        public Node re() {
            return first;
        }

        public class Node {
            Node next;
            E t;

            public Node(E e) {
                this.t = e;
            }
        }
        //下面是向链表里面添加东西
        public void add(E h) {
            Node node = new Node(h);
            if (first == null) {
                first = node;
                last = node;
                size = 1;
            } else {
                last.next = node;
                last = node;
                ++size;
            }
        }
        //下面是add方法的重载
        private void add(int index,E h){
            Node temp = new Node(h);
            Node temp1=first;
            //下面if判断有点杂乱了确实，有时间再做优化。
            if (index == 0) {
                E t=first.t;
                first.t= temp.t;
                temp.t=t;
                temp.next=first.next;
                first.next=temp;
                size++;
            } else if(index==MyLink.this.size){
                for (int i = 0; i < size-1; ++i) {
                    temp1 = temp1.next;
                }
                temp1.next=temp;
            } else{
                for (int i = 0; i < index-1; ++i) {
                    temp1 = temp1.next;
                }
                temp.next = temp1.next;
                temp1.next=temp;
                size++;
            }
        }
        public void disable(int num) {
            Node temp = first;

            if (num == 0) {
                first = first.next;
            } else {
                for (int i = 0; i < num; ++i) {
                    if (i == num - 1) {
                        temp.next = temp.next.next;
                        size--;
                    }
                    temp = temp.next;
                }
            }
        }
    }


    //以下是双向迭代的实现
    public IteratorPlus<E> iteratorPlus() {

        return new IteratorPlus<E>() {
            //小浪费资源，先不管，后续再优化
            MyLinkPlus<E>.Node m = itp.re();
            MyLinkPlus<E>.Node temp;
            MyLinkPlus<E>.Node temp1;
            MyLinkPlus<E>.Node temp2;

            @Override
            public boolean hasNext() {
                if (m != null && m.next == null) {
                    temp1 = m;
                }
                temp = m;
                if (this.m != null) {
                    this.m = this.m.next;
                    return true;
                }
                return false;
            }

            @Override
            public E next() {
                return this.temp.t;
            }

            //这里自定义一个向前迭代
            public boolean hasBefore() {
                if (this.temp1 != null) {
                    temp2 = temp1;
                    temp1 = temp1.before;
                    return true;
                }
                return false;
            }

            public E before() {
                return this.temp2.t;
            }


        };
    }

    //双向链表
    public class MyLinkPlus<E> {
        //下面定义一个变量来存储链表的长度，每添加一个加一
        private int size = 0;

        private Node first;
        private Node last;

        public Node re() {
            return first;
        }

        private class Node {
            private Node next;
            private Node before;
            private E t;

            public Node(E t) {
                this.t = t;
            }
        }

        public void add(E h) {
            Node node = new Node(h);
            if (first == null) {
                first = node;
                last = node;
            } else {
                last.next = node;
                last.next.before = last;
                last = node;
            }
        }

        public void disable(int num) {
            Node temp = first;

            if (num == 0) {
                first = first.next;
                size--;
            } else {
                for (int i = 0; i < num; ++i) {
                    if (i == num - 1) {
                        temp.next = temp.next.next;
                        size--;
                    }
                    temp = temp.next;
                }
            }
        }

        private void add(int index,E h){
           Node temp = new Node(h);
            Node temp1=first;
            if (index == 0) {
                E t=first.t;
                first.t= temp.t;
                temp.t=t;
                temp.next=first.next;
                first.next=temp;
                size++;
            } else if(index==this.size){
                for (int i = 0; i < size-1; ++i) {
                    temp1 = temp1.next;
                }
                temp1.next=temp;
            } else{
                for (int i = 0; i < index-1; ++i) {
                    temp1 = temp1.next;//0 1 2 3
                }
                temp.next = temp1.next;
                temp1.next=temp;
                size++;
            }
        }
    }
}