struct Node {
  int data;
  
  struct Node *next;
  struct Node *prev;
};

struct DoubleLinkedList {
  struct Node *head;
  struct Node *tail;
};


struct DoubleLinkedList *insert(struct DoubleLinkedList *, int);
struct Node *deleteNode(struct DoubleLinkedList *, int);
void printAll(struct DoubleLinkedList *);
int getLength(struct DoubleLinkedList *);

