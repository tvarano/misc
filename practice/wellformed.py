def well_formed(s): 
    open = 0

    for c in s: 
        if c == '(': 
            open += 1
        elif c == ')':
             open -= 1
        if open < 0: 
            return False
    
    return open == 0


print(well_formed("()()()()"))
print(well_formed("((((()))))"))
print(well_formed("((((()"))
print(well_formed("(((()())"))
print(well_formed(")"))
