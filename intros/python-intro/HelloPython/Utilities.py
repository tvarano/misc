
#getFactors


def get_factors(x):
    arr = [1]
    for i in range(2, x + 1):
        if x % i == 0:
            arr.append(i)
    return arr


def is_prime(x):
    return len(get_factors(x)) <= 2


def get_email(name):
    index = name.find(' ')
    if index == -1:
        return "put a space in ur name"
    return name[index+1:index+7].lower() + name[:2].lower() + "@pascack.org"


num = input("Input a number: ")
print(num, "has factors", str(get_factors(int(num))))
print("It is prime." if is_prime(int(num)) else "It is not prime")
print("Your email is ", get_email(input("Input your full name: ")), sep="")
