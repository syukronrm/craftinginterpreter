#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#include "linked-list.h"

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

struct Node *deleteNode(struct DoubleLinkedList *ll, int data) {
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

int getLength(struct DoubleLinkedList *ll) {
  struct Node *current = NULL;
  int length = 0;
  for (current = ll->head; current != NULL; 
        length += 1,
        current = current->next
      ) {}
  return length;
}
