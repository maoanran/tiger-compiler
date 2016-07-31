// This is automatically generated by the Tiger compiler.
// Do NOT modify!

// Control-flow Graph

// structures
struct LinkedList
{
	struct LinkedList_vtable *vptr;
	int isObjOrArray;
	unsigned length;
	void * forwarding;
};
struct Element
{
	struct Element_vtable *vptr;
	int isObjOrArray;
	unsigned length;
	void * forwarding;
	int Age;
	int Salary;
	int Married;
};
struct List
{
	struct List_vtable *vptr;
	int isObjOrArray;
	unsigned length;
	void * forwarding;
	struct Element * elem;
	struct List * next;
	int end;
};
struct LL
{
	struct LL_vtable *vptr;
	int isObjOrArray;
	unsigned length;
	void * forwarding;
};
// vtables structures
struct LinkedList_vtable
{
	char * LinkedList_gc_map;
};

struct Element_vtable
{
	char * Element_gc_map;
	int (*Element_Init)();
	int (*Element_GetAge)();
	int (*Element_GetSalary)();
	int (*Element_GetMarried)();
	int (*Element_Equal)();
	int (*Element_Compare)();
};

struct List_vtable
{
	char * List_gc_map;
	int (*List_Init)();
	int (*List_InitNew)();
	struct List * (*List_Insert)();
	int (*List_SetNext)();
	struct List * (*List_Delete)();
	int (*List_Search)();
	int (*List_GetEnd)();
	struct Element * (*List_GetElem)();
	struct List * (*List_GetNext)();
	int (*List_Print)();
};

struct LL_vtable
{
	char * LL_gc_map;
	int (*LL_Start)();
};


// vtables defines
struct LinkedList_vtable LinkedList_vtable_ ;
struct Element_vtable Element_vtable_ ;
struct List_vtable List_vtable_ ;
struct LL_vtable LL_vtable_ ;

// methods
void * frame_prev;

struct Element_Init_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int Element_Init(struct Element * this, int v_Age, int v_Salary, int v_Married)
{

	char * Element_Init_arguments_gc_map = "1000";
	char * Element_Init_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct Element_Init_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = Element_Init_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = Element_Init_locals_gc_map;

L_0:



	this->Age = v_Age;
	this->Salary = v_Salary;
	this->Married = v_Married;
	frame_prev = frame.frame_prev;
	return 1;
}

struct Element_GetAge_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int Element_GetAge(struct Element * this)
{

	char * Element_GetAge_arguments_gc_map = "1";
	char * Element_GetAge_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct Element_GetAge_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = Element_GetAge_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = Element_GetAge_locals_gc_map;

L_1:
	frame_prev = frame.frame_prev;
	return this->Age;
}

struct Element_GetSalary_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int Element_GetSalary(struct Element * this)
{

	char * Element_GetSalary_arguments_gc_map = "1";
	char * Element_GetSalary_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct Element_GetSalary_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = Element_GetSalary_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = Element_GetSalary_locals_gc_map;

L_2:
	frame_prev = frame.frame_prev;
	return this->Salary;
}

struct Element_GetMarried_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int Element_GetMarried(struct Element * this)
{

	char * Element_GetMarried_arguments_gc_map = "1";
	char * Element_GetMarried_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct Element_GetMarried_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = Element_GetMarried_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = Element_GetMarried_locals_gc_map;

L_3:
	frame_prev = frame.frame_prev;
	return this->Married;
}

struct Element_Equal_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct Element * x_1;
	struct Element * x_2;
	struct Element * x_3;
	struct Element * x_4;
	struct Element * x_5;
	struct Element * x_6;
};

int Element_Equal(struct Element * this, struct Element * other)
{
	int ret_val;
	int aux01;
	int aux02;
	int nt;
	int x_45;
	int x_46;
	int x_47;
	int x_48;
	int x_49;
	int x_50;

	char * Element_Equal_arguments_gc_map = "11";
	char * Element_Equal_locals_gc_map = "111111";
	//put the GC stack frame onto the call stack.
	struct Element_Equal_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = Element_Equal_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = Element_Equal_locals_gc_map;

L_4:


	ret_val = 1;
	x_45 = other->vptr->Element_GetAge(other);
	aux01 = x_45;
	x_46 = this->vptr->Element_Compare(this, aux01, this->Age);
	if (!x_46)
	  goto L_5;
	else
	  goto L_6;
L_5:
	ret_val = 0;
	goto L_7;
L_6:
	x_47 = other->vptr->Element_GetSalary(other);
	aux02 = x_47;
	x_48 = this->vptr->Element_Compare(this, aux02, this->Salary);
	if (!x_48)
	  goto L_8;
	else
	  goto L_9;
L_8:
	ret_val = 0;
	goto L_10;
L_9:
	if (this->Married)
	  goto L_11;
	else
	  goto L_12;
L_11:
	x_49 = other->vptr->Element_GetMarried(other);
	if (!x_49)
	  goto L_14;
	else
	  goto L_15;
L_14:
	ret_val = 0;
	goto L_16;
L_15:
	goto L_16;
L_16:
	goto L_13;
L_12:
	x_50 = other->vptr->Element_GetMarried(other);
	if (x_50)
	  goto L_17;
	else
	  goto L_18;
L_17:
	ret_val = 0;
	goto L_19;
L_18:
	goto L_19;
L_19:
	goto L_13;
L_13:
	goto L_10;
L_10:
	goto L_7;
L_7:
	frame_prev = frame.frame_prev;
	return ret_val;
}

struct Element_Compare_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int Element_Compare(struct Element * this, int num1, int num2)
{
	int retval;
	int aux02;
	int x_51;
	int x_52;
	int x_53;

	char * Element_Compare_arguments_gc_map = "100";
	char * Element_Compare_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct Element_Compare_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = Element_Compare_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = Element_Compare_locals_gc_map;

L_20:


	x_51 = num2 + 1;
	aux02 = x_51;
	x_52 = num1 < num2;
	if (x_52)
	  goto L_21;
	else
	  goto L_22;
L_21:
	retval = 0;
	goto L_23;
L_22:
	x_53 = num1 < aux02;
	if (!x_53)
	  goto L_24;
	else
	  goto L_25;
L_24:
	retval = 0;
	goto L_26;
L_25:
	retval = 1;
	goto L_26;
L_26:
	goto L_23;
L_23:
	frame_prev = frame.frame_prev;
	return retval;
}

struct List_Init_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int List_Init(struct List * this)
{

	char * List_Init_arguments_gc_map = "1";
	char * List_Init_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct List_Init_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_Init_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_Init_locals_gc_map;

L_27:
	this->end = 1;
	frame_prev = frame.frame_prev;
	return 1;
}

struct List_InitNew_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int List_InitNew(struct List * this, struct Element * v_elem, struct List * v_next, int v_end)
{

	char * List_InitNew_arguments_gc_map = "1110";
	char * List_InitNew_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct List_InitNew_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_InitNew_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_InitNew_locals_gc_map;

L_28:



	this->end = v_end;
	this->elem = v_elem;
	this->next = v_next;
	frame_prev = frame.frame_prev;
	return 1;
}

struct List_Insert_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct List * aux03;
	struct List * aux02;
	struct List * x_7;
	struct List * x_54;
};

struct List * List_Insert(struct List * this, struct Element * new_elem)
{
	int ret_val;
	int x_55;

	char * List_Insert_arguments_gc_map = "11";
	char * List_Insert_locals_gc_map = "1111";
	//put the GC stack frame onto the call stack.
	struct List_Insert_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_Insert_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_Insert_locals_gc_map;

L_29:


	frame.aux03 = this;
	frame.x_54 = ((struct List*)(Tiger_new (&List_vtable_, sizeof(struct List))));
	frame.aux02 = frame.x_54;
	x_55 = frame.aux02->vptr->List_InitNew(frame.aux02, new_elem, frame.aux03, 0);
	frame_prev = frame.frame_prev;
	return frame.aux02;
}

struct List_SetNext_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int List_SetNext(struct List * this, struct List * v_next)
{

	char * List_SetNext_arguments_gc_map = "11";
	char * List_SetNext_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct List_SetNext_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_SetNext_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_SetNext_locals_gc_map;

L_30:

	this->next = v_next;
	frame_prev = frame.frame_prev;
	return 1;
}

struct List_Delete_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct List * my_head;
	struct List * aux01;
	struct List * prev;
	struct Element * var_elem;
	struct Element * x_8;
	struct List * x_9;
	struct List * x_10;
	struct List * x_11;
	struct List * x_12;
	struct List * x_13;
	struct List * x_14;
	struct List * x_59;
	struct List * x_61;
	struct List * x_62;
	struct Element * x_64;
};

struct List * List_Delete(struct List * this, struct Element * e)
{
	int ret_val;
	int aux05;
	int var_end;
	int aux04;
	int nt;
	int x_56;
	int x_57;
	int x_58;
	int x_60;
	int x_63;

	char * List_Delete_arguments_gc_map = "11";
	char * List_Delete_locals_gc_map = "111111111111111";
	//put the GC stack frame onto the call stack.
	struct List_Delete_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_Delete_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_Delete_locals_gc_map;

L_31:


	frame.my_head = this;
	ret_val = 0;
	aux04 = -1;
	frame.aux01 = this;
	frame.prev = this;
	var_end = this->end;
	frame.var_elem = this->elem;
	goto L_34;
L_34:
	x_56 = !var_end && !ret_val;
	if (x_56)
	  goto L_32;
	else
	  goto L_33;
L_32:
	x_57 = e->vptr->Element_Equal(e, frame.var_elem);
	if (x_57)
	  goto L_35;
	else
	  goto L_36;
L_35:
	ret_val = 1;
	x_58 = aux04 < 0;
	if (x_58)
	  goto L_38;
	else
	  goto L_39;
L_38:
	frame.x_59 = frame.aux01->vptr->List_GetNext(frame.aux01);
	frame.my_head = frame.x_59;
	goto L_40;
L_39:
	System_out_println (-555);

	frame.x_61 = frame.aux01->vptr->List_GetNext(frame.aux01);
	x_60 = frame.prev->vptr->List_SetNext(frame.prev, frame.x_61);
	System_out_println (-555);

	goto L_40;
L_40:
	goto L_37;
L_36:
	goto L_37;
L_37:
	if (!ret_val)
	  goto L_41;
	else
	  goto L_42;
L_41:
	frame.prev = frame.aux01;
	frame.x_62 = frame.aux01->vptr->List_GetNext(frame.aux01);
	frame.aux01 = frame.x_62;
	x_63 = frame.aux01->vptr->List_GetEnd(frame.aux01);
	var_end = x_63;
	frame.x_64 = frame.aux01->vptr->List_GetElem(frame.aux01);
	frame.var_elem = frame.x_64;
	aux04 = 1;
	goto L_43;
L_42:
	goto L_43;
L_43:
	goto L_34;
L_33:
	frame_prev = frame.frame_prev;
	return frame.my_head;
}

struct List_Search_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct List * aux01;
	struct Element * var_elem;
	struct Element * x_15;
	struct List * x_16;
	struct List * x_17;
	struct List * x_18;
	struct List * x_66;
	struct Element * x_68;
};

int List_Search(struct List * this, struct Element * e)
{
	int int_ret_val;
	int var_end;
	int nt;
	int x_65;
	int x_67;

	char * List_Search_arguments_gc_map = "11";
	char * List_Search_locals_gc_map = "11111111";
	//put the GC stack frame onto the call stack.
	struct List_Search_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_Search_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_Search_locals_gc_map;

L_44:


	int_ret_val = 0;
	frame.aux01 = this;
	var_end = this->end;
	frame.var_elem = this->elem;
	goto L_47;
L_47:
	if (!var_end)
	  goto L_45;
	else
	  goto L_46;
L_45:
	x_65 = e->vptr->Element_Equal(e, frame.var_elem);
	if (x_65)
	  goto L_48;
	else
	  goto L_49;
L_48:
	int_ret_val = 1;
	goto L_50;
L_49:
	goto L_50;
L_50:
	frame.x_66 = frame.aux01->vptr->List_GetNext(frame.aux01);
	frame.aux01 = frame.x_66;
	x_67 = frame.aux01->vptr->List_GetEnd(frame.aux01);
	var_end = x_67;
	frame.x_68 = frame.aux01->vptr->List_GetElem(frame.aux01);
	frame.var_elem = frame.x_68;
	goto L_47;
L_46:
	frame_prev = frame.frame_prev;
	return int_ret_val;
}

struct List_GetEnd_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

int List_GetEnd(struct List * this)
{

	char * List_GetEnd_arguments_gc_map = "1";
	char * List_GetEnd_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct List_GetEnd_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_GetEnd_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_GetEnd_locals_gc_map;

L_51:
	frame_prev = frame.frame_prev;
	return this->end;
}

struct List_GetElem_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

struct Element * List_GetElem(struct List * this)
{

	char * List_GetElem_arguments_gc_map = "1";
	char * List_GetElem_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct List_GetElem_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_GetElem_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_GetElem_locals_gc_map;

L_52:
	frame_prev = frame.frame_prev;
	return this->elem;
}

struct List_GetNext_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
};

struct List * List_GetNext(struct List * this)
{

	char * List_GetNext_arguments_gc_map = "1";
	char * List_GetNext_locals_gc_map = "";
	//put the GC stack frame onto the call stack.
	struct List_GetNext_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_GetNext_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_GetNext_locals_gc_map;

L_53:
	frame_prev = frame.frame_prev;
	return this->next;
}

struct List_Print_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct List * aux01;
	struct Element * var_elem;
	struct Element * x_19;
	struct List * x_20;
	struct List * x_21;
	struct List * x_22;
	struct List * x_70;
	struct Element * x_72;
};

int List_Print(struct List * this)
{
	int var_end;
	int x_69;
	int x_71;

	char * List_Print_arguments_gc_map = "1";
	char * List_Print_locals_gc_map = "11111111";
	//put the GC stack frame onto the call stack.
	struct List_Print_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = List_Print_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = List_Print_locals_gc_map;

L_54:

	frame.aux01 = this;
	var_end = this->end;
	frame.var_elem = this->elem;
	goto L_57;
L_57:
	if (!var_end)
	  goto L_55;
	else
	  goto L_56;
L_55:
	x_69 = frame.var_elem->vptr->Element_GetAge(frame.var_elem);
	System_out_println (x_69);

	frame.x_70 = frame.aux01->vptr->List_GetNext(frame.aux01);
	frame.aux01 = frame.x_70;
	x_71 = frame.aux01->vptr->List_GetEnd(frame.aux01);
	var_end = x_71;
	frame.x_72 = frame.aux01->vptr->List_GetElem(frame.aux01);
	frame.var_elem = frame.x_72;
	goto L_57;
L_56:
	frame_prev = frame.frame_prev;
	return 1;
}

struct LL_Start_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct List * head;
	struct List * last_elem;
	struct Element * el01;
	struct Element * el02;
	struct Element * el03;
	struct List * x_23;
	struct List * x_24;
	struct List * x_25;
	struct Element * x_26;
	struct List * x_27;
	struct List * x_28;
	struct Element * x_29;
	struct List * x_30;
	struct List * x_31;
	struct Element * x_32;
	struct List * x_33;
	struct List * x_34;
	struct Element * x_35;
	struct List * x_36;
	struct List * x_37;
	struct Element * x_38;
	struct List * x_39;
	struct List * x_40;
	struct List * x_41;
	struct List * x_42;
	struct List * x_43;
	struct List * x_44;
	struct List * x_73;
	struct Element * x_77;
	struct List * x_79;
	struct Element * x_81;
	struct List * x_83;
	struct Element * x_85;
	struct List * x_87;
	struct Element * x_89;
	struct Element * x_93;
	struct List * x_95;
	struct List * x_97;
	struct List * x_99;
};

int LL_Start(struct LL * this)
{
	int aux01;
	int x_74;
	int x_75;
	int x_76;
	int x_78;
	int x_80;
	int x_82;
	int x_84;
	int x_86;
	int x_88;
	int x_90;
	int x_91;
	int x_92;
	int x_94;
	int x_96;
	int x_98;
	int x_100;

	char * LL_Start_arguments_gc_map = "1";
	char * LL_Start_locals_gc_map = "111111111111111111111111111111111111111";
	//put the GC stack frame onto the call stack.
	struct LL_Start_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = LL_Start_arguments_gc_map;
	frame.arguments_base_address = (int *)&this;
	frame.locals_gc_map = LL_Start_locals_gc_map;

L_58:
	frame.x_73 = ((struct List*)(Tiger_new (&List_vtable_, sizeof(struct List))));
	frame.last_elem = frame.x_73;
	x_74 = frame.last_elem->vptr->List_Init(frame.last_elem);
	frame.head = frame.x_73;
	x_75 = frame.head->vptr->List_Init(frame.head);
	x_76 = frame.head->vptr->List_Print(frame.head);
	frame.x_77 = ((struct Element*)(Tiger_new (&Element_vtable_, sizeof(struct Element))));
	frame.el01 = frame.x_77;
	x_78 = frame.el01->vptr->Element_Init(frame.el01, 25, 37000, 0);
	frame.x_79 = frame.head->vptr->List_Insert(frame.head, frame.el01);
	frame.head = frame.x_79;
	x_80 = frame.head->vptr->List_Print(frame.head);
	System_out_println (10000000);

	frame.x_81 = ((struct Element*)(Tiger_new (&Element_vtable_, sizeof(struct Element))));
	frame.el01 = frame.x_81;
	x_82 = frame.el01->vptr->Element_Init(frame.el01, 39, 42000, 1);
	frame.el02 = frame.x_81;
	frame.x_83 = frame.head->vptr->List_Insert(frame.head, frame.el01);
	frame.head = frame.x_83;
	x_84 = frame.head->vptr->List_Print(frame.head);
	System_out_println (10000000);

	frame.x_85 = ((struct Element*)(Tiger_new (&Element_vtable_, sizeof(struct Element))));
	frame.el01 = frame.x_85;
	x_86 = frame.el01->vptr->Element_Init(frame.el01, 22, 34000, 0);
	frame.x_87 = frame.head->vptr->List_Insert(frame.head, frame.el01);
	frame.head = frame.x_87;
	x_88 = frame.head->vptr->List_Print(frame.head);
	frame.x_89 = ((struct Element*)(Tiger_new (&Element_vtable_, sizeof(struct Element))));
	frame.el03 = frame.x_89;
	x_90 = frame.el03->vptr->Element_Init(frame.el03, 27, 34000, 0);
	x_91 = frame.head->vptr->List_Search(frame.head, frame.el02);
	System_out_println (x_91);

	x_92 = frame.head->vptr->List_Search(frame.head, frame.el03);
	System_out_println (x_92);

	System_out_println (10000000);

	frame.x_93 = ((struct Element*)(Tiger_new (&Element_vtable_, sizeof(struct Element))));
	frame.el01 = frame.x_93;
	x_94 = frame.el01->vptr->Element_Init(frame.el01, 28, 35000, 0);
	frame.x_95 = frame.head->vptr->List_Insert(frame.head, frame.el01);
	frame.head = frame.x_95;
	x_96 = frame.head->vptr->List_Print(frame.head);
	System_out_println (2220000);

	frame.x_97 = frame.head->vptr->List_Delete(frame.head, frame.el02);
	frame.head = frame.x_97;
	x_98 = frame.head->vptr->List_Print(frame.head);
	System_out_println (33300000);

	frame.x_99 = frame.head->vptr->List_Delete(frame.head, frame.el01);
	frame.head = frame.x_99;
	x_100 = frame.head->vptr->List_Print(frame.head);
	System_out_println (44440000);

	frame_prev = frame.frame_prev;
	return 0;
}

// vtables
struct LinkedList_vtable LinkedList_vtable_ = 
{
	"",
};

struct Element_vtable Element_vtable_ = 
{
	"000",
	Element_Init,
	Element_GetAge,
	Element_GetSalary,
	Element_GetMarried,
	Element_Equal,
	Element_Compare,
};

struct List_vtable List_vtable_ = 
{
	"110",
	List_Init,
	List_InitNew,
	List_Insert,
	List_SetNext,
	List_Delete,
	List_Search,
	List_GetEnd,
	List_GetElem,
	List_GetNext,
	List_Print,
};

struct LL_vtable LL_vtable_ = 
{
	"",
	LL_Start,
};


// main method

struct Tiger_main_gc_frame{
	void * frame_prev;
	int * arguments_base_address;
	char * arguments_gc_map;
	char * locals_gc_map;
	struct LL * x_0;
	struct LL * x_102;
};

int Tiger_main ()
{
	int x_101;
	char * tiger_main_arguments_gc_map = "";
	char * tiger_main_locals_gc_map = "11";
	//put the GC stack frame onto the call stack.
	struct Tiger_main_gc_frame frame;
	//push this frame onto the GC stack by setting up "prev".
	frame.frame_prev = frame_prev;
	frame_prev = &frame;
	//setting up memory GC maps and corresponding base addresses
	frame.arguments_gc_map = tiger_main_arguments_gc_map;
	frame.locals_gc_map = tiger_main_locals_gc_map;

L_59:
	frame.x_102 = ((struct LL*)(Tiger_new (&LL_vtable_, sizeof(struct LL))));
	x_101 = frame.x_102->vptr->LL_Start(frame.x_102);
	System_out_println (x_101);

	frame_prev = frame.frame_prev;
	return 0;
}




