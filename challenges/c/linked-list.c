#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

struct node {
  int data;
  
  struct node *next;
  struct node *prev;
};

struct node *insert(struct node *head, int data) {
  struct node *newNode = (struct node*) malloc(sizeof(struct node));
  newNode->data = data;
  newNode->next = NULL;
  

  if (head == NULL) {
    newNode->prev = NULL;
    head = newNode;
  } else {
    struct node *current = NULL;
    for (current = head; current->next != NULL; current = current->next) {}

    newNode->prev = current;
    current->next = newNode;
  }

  return head;
}

int main() {
  struct node *head = NULL;

  head = insert(head, 100);
  head = insert(head, 200);
  head = insert(head, 300);

  struct node *current = NULL;
  for (current = head; current != NULL; current = current->next) {
    printf("data = %d\n", current->data);
  }


  return 0;
}

