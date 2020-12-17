def rain(arr): 
    left = [0] * len(arr)
    m = 0
    ret = 0
    right = [0] * len(arr)
    for i in range(len(arr)): 
        m = max(m, arr[i])
        left[i] = m
    for i in reversed(range(len(arr))): 
        m = max(m, arr[i])
        right[i] = m
    for i in range(len(arr)):
        ret += min(left[i], right[i]) - arr[i]

    return ret