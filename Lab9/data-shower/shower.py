from tkinter import *
root = Tk()
cans=Canvas(root,height=500,width=600)
cans.pack()
delay = 1  # milliseconds
array_list=[] # this will be list of arrays

def get_new_data():
    new_data=[]
    #new_data = #whatever write code here how you get next new list from somewhere
    array_list.append(raw_data)    
    return array_list

def get_lits(x):
    get_new_data()
    return array_list[x-2]

def get_color(y, x):
    max_num = 15
    raw_array=get_data(x)
    c = raw_array[y] * 255 / max_num
    colorname = '#%02x%02x%02x' % (c, c, c)
    return colorname

def draw_line(i=2, j=0):
    if i <= 600:
        if j<=500:
            cans.create_line(i,j,i+1,j, fill=get_color(j, i))
            root.after(delay, draw_line, i+0, j+1)
        elif j>150:
            j=0
            root.after(delay, draw_line, i+1)
draw_line()       
root.mainloop()