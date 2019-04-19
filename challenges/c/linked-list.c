#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

struct Node {
  int data;
  
  struct Node *next;
  struct Node *prev;
};

struct DoubleLinkedList {
  struct Node *head;
  struct Node *tail;
};

struct DoubleLinkedList *insert(struct DoubleLinkedList *ll, int data) {
  struct Node *newNode = (struct Node*) malloc(sizeof(struct Node));
  newNode->data = data;
  newNode->next = NULL;
  
  if (ll->head == NULL) {
    newNode->prev = NULL;
    ll->head = newNode;
    ll->tail = newNode;
  } else {
    newNode->prev = ll->tail;
    ll->tail->next = newNode;
    ll->tail = newNode;
  }

  return ll;
}

struct Node *delete(struct DoubleLinkedList *ll, int data) {
  printf("  delete %d\n", data);

  struct Node *current = NULL;
  for (current = ll->head; current != NULL; current = current->next) {
    if (current->data == data)
      break;
  }

  if (current) {
    if (current == ll->head) {
      ll->head = ll->head->next;
      if (ll->head == NULL)
        ll->tail = NULL;
    } else if (current == ll->tail) {
      ll->tail = ll->tail->prev;
      ll->tail->next = NULL;
    } else {
      current->prev->next = current->next;
      current->next->prev = current->prev;
    }

    current->next = NULL;
    current->prev = NULL;
  }

  return current;
}

void printAll(struct DoubleLinkedList *ll) {
  struct Node *current = NULL;
  for (current = ll->head; current != NULL; current = current->next) {
    printf("data = %d\n", current->data);
  }
  printf("\n");
}

int main() {
  struct DoubleLinkedList *ll = (struct DoubleLinkedList*) malloc(sizeof(struct DoubleLinkedList));
  ll->head = NULL;
  ll->tail = NULL;

  ll = insert(ll, 100);
  ll = insert(ll, 200);
  ll = insert(ll, 300);
  ll = insert(ll, 400);
  printAll(ll);

  struct Node *deletedNode = delete(ll, 100);
  printAll(ll);
  deletedNode = delete(ll, 400);
  deletedNode = delete(ll, 400);
  printAll(ll);
  deletedNode = delete(ll, 300);
  printAll(ll);
  deletedNode = delete(ll, 200);
  printAll(ll);

  return 0;
}

