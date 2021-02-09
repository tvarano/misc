def radixSort(arr)
    sorted = arr.dup
    longest = 0
    for s in arr
        if s.length > longest
            longest = s.length
        end
    end


    puts "longest len #{}", longest
    for i in (longest-1).downto(0)
        boxes = {}

        ([' '] + (('a'..'z').to_a)).each {|key|
            boxes[key] = []
        }
        
        sorted.each {|str|
            if (i >= str.length)
                boxes[' '].push str
            else 
                boxes[str[i]].push str
            end 
        }

        puts i
        puts boxes

        sorted = []

        boxes.each {|key, value|
            sorted.concat(value)
        }
    end

    sorted
end


puts "enter array separated by commas --> a,b,c"
a = gets.chomp.downcase!.split(",")

print(radixSort(a))