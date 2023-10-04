/*************************************************
* developer: Evgenii
* reviewer: Moshe
* date: 25.06.2023
*
**************************************************/
#include <stdlib.h> /*malloc*/
#include <stdio.h> /*printf*/
#include <string.h>/*strcpy*/
#include <assert.h>

#include "Java2C.h"

#define OBJ_CLASS_NAME ("Object")
#define ANIMAL_CLASS_NAME ("Animal")
#define DOG_CLASS_NAME ("Dog")
#define CAT_CLASS_NAME ("Cat")
#define LGD_CLASS_NAME ("Legendary")
#define ERROR (-1)
#define SUCCESS (0)
#define TO_STRING_SIZE (100)

char str_to_buff[TO_STRING_SIZE];
static int animal_counter = 0;


/***********************Typedef Declaration***************/

typedef char *(*toStringFunc)(Object *obj, char *buff);
typedef void (*sayHelloFunc)(Animal *obj);
typedef int (*getNumMastersFunc)(Animal *obj);
typedef void (*finalizeFunc)(Object *o);

Class *objectClass = NULL;
Class *animalClass = NULL;
Class *catClass = NULL;
Class *dogClass = NULL;
Class *legendClass = NULL;

/**************************Class Loading**********************/
static int ClassLoader(Class **class, char *className, Class *super, void **table);
static void LoadObjectClass();
static void LoadAnimalClass();
static void LoadDogClass();
static void LoadCatClass();
static void LoadLegendaryClass();

/***************************Utility functions*********************/
static void FillAnimalPart(Animal *a, int masters_num, char *msg);
static void FillCatPart(Cat *c, int num_masters, char *color);
static void PrintDefaultAnimal(Animal *newA);
static void PrintCatColor(char *color);
static Animal *GetEmptyAnimal();
static LegendaryAnimal *GetEmptyLGD();
static Cat *GetEmptyCat();

/********************************Global variables Init*********************/
void *funcTableObject[FUNC_NUM] = {ObjectToString, FinalizeObject};
void *funcTableAnimal[FUNC_NUM] = {AnimalToString, FinalizeAnimal, SayHelloAnimal, ShowCounter};
void *funcTableCat[FUNC_NUM] = {CatToString, FinalizeCat, SayHelloAnimal, ShowCounter};
void *funcTableDog[FUNC_NUM] = {DogToString, FinalizeDog, SayHelloDog, ShowCounter};
void *funcTableLgd[FUNC_NUM] = {LegendToString, FinalizeLegend, SayHelloLegend, ShowCounter};

/**********************ToString Implementation*********************/
char *SupertoString(Object *obj, Class *super, char *buff)
{
    return ((toStringFunc)super->v_func_table[0])(obj, buff);
}

char *ToString(Object *obj, char *buff)
{
    /*casting to function pointer of type
      void (*anyname)(Object *obj, char *buff)
      to a variable of type void* from the table v_func_table */
    return ((toStringFunc)obj->obj_class->v_func_table[0])(obj, buff);
}

char *ObjectToString(void *obj, char *buff)
{
    Object *ob = (Object *)obj;
    sprintf(buff, "%s@%x", ob->obj_class->class_name, (unsigned int)(size_t)obj);

    return buff;
}

char *AnimalToString(void *obj, char *buff)
{
    Animal *animal = (Animal *)obj;
    sprintf(buff, "Animal with ID: %d\n", animal->ID);

    return buff;
}

char *DogToString(void *obj, char *buff)
{
    Dog *dog = (Dog *)obj;
    sprintf(buff, "Dog with ID: %d\n", dog->animal.ID);

    return buff;
}

char *CatToString(void *obj, char *buff)
{
    Cat *cat = (Cat *)obj;
    sprintf(buff, "Cat with ID: %d\n", cat->animal.ID);

    return buff;
}

char *LegendToString(void *obj, char *buff)
{
    LegendaryAnimal *lgd = (LegendaryAnimal *)obj;
    sprintf(buff, "LegendaryAnimal with ID: %d\n", lgd->cat.animal.ID);

    return buff;
}

/***********************Finalize function*****************************/

void Finalize(Object *obj)
{
    ((finalizeFunc)obj->obj_class->v_func_table[1])(obj);
}

void FinalizeObject(Object *obj)
{
    printf("finalize object\n");
    free(obj);
}

void FinalizeAnimal(Object *obj)
{
    printf("finalize Animal with ID: %d\n", ((Animal *)obj)->ID);
    ((finalizeFunc)animalClass->super->v_func_table[1])(obj);
}

void FinalizeCat(Object *obj)
{
    printf("finalize Cat with ID: %d\n", ((Cat *)obj)->animal.ID);
    ((finalizeFunc)catClass->super->v_func_table[1])(obj);
}

void FinalizeDog(Object *obj)
{
    printf("finalize Dog with ID: %d\n", ((Dog *)obj)->animal.ID);
    ((finalizeFunc)dogClass->super->v_func_table[1])(obj);
}

void FinalizeLegend(Object *obj)
{
    printf("finalize Legendary animal with ID: %d\n", ((LegendaryAnimal *)obj)->cat.animal.ID);
    ((finalizeFunc)legendClass->super->v_func_table[1])(obj);
}

/**********************************Say Hello func***************************/

void SayHello(Animal *a)
{
    ((sayHelloFunc)a->obj.obj_class->v_func_table[2])(a);
}

void SayHelloAnimal(Animal *a)
{
    printf("Animal Hello !\n");
    printf("I have %d legs\n", a->num_legs);
}

void SayHelloDog(Animal *a)
{
    Dog *d = (Dog *)a;
    printf("Dog Hello\n");
    printf("I have %d legs\n", d->num_legs);
}
void SayHelloLegend(Animal *a)
{
    printf("Legendary Hello\n");
}

/*************************Utility functions*******************/

void ShowCounter()
{
    printf("%d\n", animal_counter);
}

int getNumMasters(Animal *a)
{
    return a->num_masters;
}

static void NonStaticBlockAnimal()
{
    printf("Instance initialization block Animal\n");
}

static void PrintCatColor(char *color)
{
    printf("Cat Ctor with color: %s\n", color);
}

static void PrintDefaultAnimal(Animal *newA)
{
    SayHello(newA);
    ShowCounter();
    printf("%s", ToString((Object *)newA, str_to_buff));
    printf("%s\n", SupertoString((Object *)newA, animalClass->super, str_to_buff));
}

static void FillAnimalPart(Animal *a, int masters_num, char *msg)
{
    NonStaticBlockAnimal();
    printf("%s\n", msg);

    a->ID = ++animal_counter;
    a->num_masters = masters_num;
    a->num_legs = 5;
    PrintDefaultAnimal(a);
}

static void FillCatPart(Cat *c, int num_masters, char *color)
{
    c->num_masters = num_masters;
    strcpy(c->color, color);
    PrintCatColor(color);

    if (num_masters == 2)
    {
        printf("Cat Ctor\n");
    }
}

/***********************Creation function*******************/

Animal *NewAnimal()
{
    Animal *newA = GetEmptyAnimal();
    FillAnimalPart(newA, 1, "Animal Ctor");

    return newA;
}

Animal *NewAnimalMaster(int num_masters)
{
    Animal *newA = GetEmptyAnimal();
    FillAnimalPart(newA, num_masters, "Animal Ctor int");

    return newA;
}

Dog *NewDog()
{
    Dog *newD = NULL;

    if (NULL == objectClass)
    {
        LoadObjectClass();
    }
    if (NULL == animalClass)
    {
        LoadAnimalClass();
    }
    if (NULL == dogClass)
    {
        LoadDogClass();
    }

    newD = (Dog *)malloc(sizeof(Dog));

    if (NULL == newD)
    {
        perror("Dog creating Error\n");
        return NULL;
    }

    newD->animal.obj.obj_class = dogClass;
    newD->num_legs = 4;
    FillAnimalPart(&newD->animal, 2, "Animal Ctor int");
    printf("Instance initialization block Dog\n");
    printf("Dog Ctor\n");

    return newD;
}

Cat *NewCat()
{
    Cat *newC = GetEmptyCat();
    FillAnimalPart(&newC->animal, 1, "Animal Ctor");
    FillCatPart(newC, 2, "black");

    return newC;
}

Cat *NewCatColor(char *color)
{
    Cat *newC = GetEmptyCat();
    FillAnimalPart(&newC->animal, 1, "Animal Ctor");
    FillCatPart(newC, 5, color);

    return newC;
}

LegendaryAnimal *NewLGD()
{
    LegendaryAnimal *lgd = GetEmptyLGD();
    FillAnimalPart(&lgd->cat.animal, 1, "Animal Ctor");
    FillCatPart(&lgd->cat, 2, "black");
    printf("Legendary Ctor\n");

    return lgd;
}

static Animal *GetEmptyAnimal()
{
    Animal *newA = NULL;

    if (NULL == objectClass)
    {
        LoadObjectClass();
    }
    if (NULL == animalClass)
    {
        LoadAnimalClass();
    }

    newA = (Animal *)malloc(sizeof(Animal));

    if (NULL == newA)
    {
        perror("Animal creating Error\n");
        return NULL;
    }
    newA->obj.obj_class = animalClass;

    return newA;
}

static LegendaryAnimal *GetEmptyLGD()
{
    LegendaryAnimal *lgd = NULL;

    if (NULL == objectClass)
    {
        LoadObjectClass();
    }
    if (NULL == animalClass)
    {
        LoadAnimalClass();
    }
    if (NULL == catClass)
    {
        LoadCatClass();
    }
    if (NULL == legendClass)
    {
        LoadLegendaryClass();
    }

    lgd = (LegendaryAnimal *)malloc(sizeof(LegendaryAnimal));

    if (NULL == lgd)
    {
        perror("LGD creating Error\n");
        return NULL;
    }

    lgd->cat.animal.obj.obj_class = legendClass;

    return lgd;
}

static Cat *GetEmptyCat()
{
    Cat *newC = NULL;

    if (NULL == objectClass)
    {
        LoadObjectClass();
    }
    if (NULL == animalClass)
    {
        LoadAnimalClass();
    }
    if (NULL == catClass)
    {
        LoadCatClass();
    }

    newC = (Cat *)malloc(sizeof(Cat));

    if (NULL == newC)
    {
        perror("Cat creating Error\n");
        return NULL;
    }

    newC->animal.obj.obj_class = catClass;

    return newC;
}

/**********************Table Loaders************************/

static void LoadObjectClass()
{
    if (SUCCESS != ClassLoader(&objectClass, OBJ_CLASS_NAME, NULL, funcTableObject))
    {
        ClassDestroyer();
    }
}

static void LoadAnimalClass()
{
    printf("Static block Animal 1\n");

    if (SUCCESS != ClassLoader(&animalClass, ANIMAL_CLASS_NAME, objectClass, funcTableAnimal))
    {
        ClassDestroyer();
    }

    printf("Static block Animal 2\n");
}

static int ClassLoader(Class **class, char *className, Class *super, void **table)
{
    *class = (Class *)malloc(sizeof(Class));

    if (NULL == class)
    {
        perror("Error while Class Loading\n");
        return ERROR;
    }

    strcpy((*class)->class_name, className);
    (*class)->super = super;
    (*class)->v_func_table = table;

    return SUCCESS;
}

static void LoadDogClass()
{
    printf("Static block Dog \n");

    if (SUCCESS != ClassLoader(&dogClass, DOG_CLASS_NAME, animalClass, funcTableDog))
    {
        ClassDestroyer();
    }
}

static void LoadCatClass()
{
    printf("Static block Cat \n");

    if (SUCCESS != ClassLoader(&catClass, CAT_CLASS_NAME, animalClass, funcTableCat))
    {
        ClassDestroyer();
    }
}

static void LoadLegendaryClass()
{
    printf("Static block Legendary Animal\n");

    if (SUCCESS != ClassLoader(&legendClass, LGD_CLASS_NAME, catClass, funcTableLgd))
    {
        ClassDestroyer();
    }
}

void ClassDestroyer()
{
    free(objectClass);
    free(animalClass);
    free(catClass);
    free(dogClass);
    free(legendClass);
}


int main()
{
    Object obj;
    Animal *an = NewAnimal();
    Dog *d = NewDog();
    Cat *c = NewCat();
    LegendaryAnimal *lgd = NewLGD();
    size_t size = 0;

    ShowCounter();

    printf("%d\n", an->ID);
    printf("%d\n", ((Animal *)d)->ID);
    printf("%d\n", ((Animal *)c)->ID);
    printf("%d\n", ((Animal *)lgd)->ID);

    Animal *arr_animals[] = {
        (Animal *)NewDog(),
        (Animal *)NewCat(),
        (Animal *)NewCatColor("White"),
        (Animal *)NewLGD(),
        (Animal *)NewAnimal()};

    size = sizeof(arr_animals) / sizeof(arr_animals[0]);

    for (size_t i = 0; i < size; ++i)
    {
        SayHello(arr_animals[i]);
        printf("%d\n", getNumMasters(arr_animals[i]));
    }

    for (size_t i = 0; i < size; ++i)
    {
        foo(arr_animals[i]);
    }

    DestroyAll(arr_animals, size, lgd, an, d, c);

    return 0;
}

void DestroyAll(Animal *arr_animals[], size_t size, LegendaryAnimal *lgd, Animal *an, Dog *d, Cat *c)
{
    for (size_t i = 0; i < size; ++i)
    {
        Finalize((Object *)arr_animals[i]);
    }

    Finalize((Object *)lgd);
    Finalize((Object *)an);
    Finalize((Object *)d);
    Finalize((Object *)c);

    ClassDestroyer();
}

void foo(Animal *a)
{
    printf("%s\n", ToString((Object *)a, str_to_buff));
}



