

#ifndef __ILRD_139_40__J2C_H__
#define __ILRD_139_40__J2C_H__

#define FUNC_NUM (6)
#define CLASS_NAME_SIZE (20)
#define COLOR_SIZE (10)

typedef struct class Class;
typedef struct object Object;
typedef struct animal Animal;
typedef struct dog Dog;
typedef struct cat Cat;
typedef struct legendary_animal LegendaryAnimal;

struct class
{
    void **v_func_table;
    char class_name[CLASS_NAME_SIZE];
    Class *super;
};

struct object
{
    Class *obj_class;
};

struct animal
{
    Object obj;
    int ID;
    int num_legs;
    int num_masters;
} ;

struct dog
{
    Animal animal;
    int num_legs;
};

struct cat
{
    Animal animal;
    int num_masters;
    char color[COLOR_SIZE];
} ;

struct legendary_animal
{
    Cat cat;
};

/****************************Creation Function********************/
Animal *NewAnimal();
Cat *NewCat();
Cat *NewCatColor(char *color);
Dog *NewDog();
LegendaryAnimal *NewLGD();
void ClassDestroyer();


/**************************Tables Function ********************/
void Finalize(Object *obj);
void FinalizeObject(Object *obj);
void FinalizeAnimal(Object *obj);
void FinalizeCat(Object *obj);
void FinalizeDog(Object *obj);
void FinalizeLegend(Object *obj);

void SayHello(Animal *obj);
void SayHelloAnimal(Animal *obj);
void SayHelloDog(Animal *obj);
void SayHelloLegend(Animal *obj);

char *ToString(Object *obj, char *buff);
char *SupertoString(Object *obj, Class *super, char *buff);
char *ObjectToString(void *obj, char *buff);
char *AnimalToString(void *obj, char *buff);
char *DogToString(void *obj, char *buff);
char *CatToString(void *obj, char *buff);
char *LegendToString(void *obj, char *buff);

/*******************Utility function*********************/
void ShowCounter();
int getNumMasters(Animal *a);
void foo(Animal *a);
void DestroyAll(Animal *arr_animals[], size_t size, LegendaryAnimal *lgd, Animal *an, Dog *d, Cat *c);




#endif
