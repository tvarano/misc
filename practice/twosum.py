def two_sum(arr, target): 
    found = {}
    ret = []
    for i in range(len(arr)):
        print(found)
        if arr[i] in found: 
            found[arr[i]].append(i)
        else: 
            found[arr[i]] = [i]

        if target - arr[i] in found: 
            for f in found[target - arr[i]]: 
                ret.append( (i, f))

    return ret



print(two_sum([1, 2, 4, 6, 3, 2, 4], 5))