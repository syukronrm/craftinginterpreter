#include <stdlib.h>
#include <assert.h>

#include "linked-list.h"

int main() {
  struct DoubleLinkedList *ll = (struct DoubleLinkedList*) malloc(sizeof(struct DoubleLinkedList));
  ll->head = NULL;
  ll->tail = NULL;

  ll = insert(ll, 100);
  ll = insert(ll, 200);
  ll = insert(ll, 300);
  ll = insert(ll, 400);

  assert(getLength(ll) == 4);

  struct Node *deletedNode = deleteNode(ll, 100);
  assert(deletedNode->data == 100);

  deletedNode = deleteNode(ll, 400);
  assert(deletedNode->data == 400);

  deletedNode = deleteNode(ll, 400);
  assert(deletedNode == NULL);

  deletedNode = deleteNode(ll, 300);
  assert(deletedNode->data == 300);

  deletedNode = deleteNode(ll, 200);
  assert(deletedNode->data == 200);

  return 0;
}
